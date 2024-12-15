import asyncio
import traceback
import mariadb
import os
import sys
import dotenv
import logging
import requests
from influxdb_client import InfluxDBClient
from influxdb_client.client.query_api import QueryApi
import json
import random
import threading
import time
import folium
import matplotlib.pyplot as plt
from datetime import datetime
import numpy as np
from confluent_kafka import Producer, Consumer, KafkaError, KafkaException
import sys
import logging
import qrcode
import os

dotenv.load_dotenv()

base_url = os.getenv("BASE_URL", "http://localhost")

INFLUXDB_URL = f"http://localhost:{os.getenv('INFLUXDB_PORT')}"
INFLUXDB_TOKEN = os.getenv('DOCKER_INFLUXDB_INIT_ADMIN_TOKEN')
INFLUXDB_ORG = os.getenv('DOCKER_INFLUXDB_INIT_ORG')
INFLUXDB_BUCKET = os.getenv('DOCKER_INFLUXDB_INIT_BUCKET')

logger = {}

def create_device_logger(device_id, level=logging.INFO):
    logger = logging.getLogger(f"device_{device_id}")
    logger.propagate = False
    handler = logging.StreamHandler()
    formatter = logging.Formatter(f'%(asctime)s - %(levelname)s - {device_id}: %(message)s')
    handler.setFormatter(formatter)
    logger.addHandler(handler)
    logger.setLevel(level)
    
    return logger

def print_qrcode(device_id):
    data = f"{base_url}/finders/{device_id}"

    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_L,
        box_size=1,
        border=1,
    )

    # Generate QR code
    qr.add_data(data)
    qr.make(fit=True)

    # Generate an image from the QR Code instance
    qr_code_ascii = qr.make_image(fill_color='black', back_color='white')
    #qr_code_ascii.show()  # Open in a image viewer

    # Print the qr code matrix on the console
    qr_code_text = qr.get_matrix()  # Obter a matriz do QR Code
    str = ""
    for row in qr_code_text:
        str += ("".join(['██' if cell else '  ' for cell in row])) + "\n"
    logger[device_id].info(f"QR Code for pet finders:\n{str}")


def get_last_battery_level(animal_id, device_id):
    client = InfluxDBClient(
        url=INFLUXDB_URL,
        token=INFLUXDB_TOKEN,
        org=INFLUXDB_ORG
    )

    query_api = client.query_api()
    query = f'''
        from(bucket: "{INFLUXDB_BUCKET}")
        |> range(start: -1d)
        |> filter(fn: (r) => r._measurement == "animal_data")
        |> filter(fn: (r) => r._field == "batteryPercentage")
        |> filter(fn: (r) => r.animalId == "{animal_id}")
        |> last()
    '''

    try:
        result = query_api.query(query=query)
        for table in result:
            for record in table.records:
                return record.get_value()
    except Exception as e:
        logger[device_id].debug(f"Error querying InfluxDB for animal ID {animal_id}: {e}")
    finally:
        client.close()

    return None


def get_animal_id(device_id):
    url = f"http://localhost/api/v1/finders/animal/{device_id}"
    try:
        response = requests.get(url)
        response.raise_for_status()
        data = response.json()
        return data['id']
    except requests.exceptions.HTTPError as e:
        print(f"HTTP Error: {e}")
    except requests.exceptions.RequestException as e:
        print(f"Error fetching animal ID: {e}")
    return None


def create_kafka_producer():
    conf = {
        'bootstrap.servers': 'localhost:9092',
        'client.id': 'animal-simulation-producer'
    }
    return Producer(conf)


def send_to_kafka(producer, topic, key, message):
    key_bytes = str(key).encode('utf-8')
    try:
        producer.produce(topic, key=key_bytes, value=message)
        producer.flush()
    except Exception as e:
        logger[key].error(f"Error sending to kafka: {e}")


def simulate_state_data(device_id, last_state):
    current_hour = datetime.now().hour
    states = ['adormir', 'acorrer', 'adescansar']

    time_probabilities = {
        'day': [0.0, 0.7, 0.3],
        'night': [0.5, 0.2, 0.3]
    }

    is_daytime = 6 <= current_hour < 18
    probabilities = time_probabilities['day'] if is_daytime else time_probabilities['night']

    animal_state = random.choices(states, probabilities)[0]

    if animal_state == last_state:
        available_states = [state for state in states if state != last_state]
        animal_state = random.choice(available_states)

    return animal_state, {
        'device_id': device_id,
        'estado': animal_state,
        'timestamp': int(time.time())
    }


def simulate_animal_speed(estado):
    speed_ranges = {
        "acorrer": (15.0, 25.0),
        "adescansar": (0.5, 3.0),
        "adormir": (0.0, 0.0)
    }

    speed_range = speed_ranges.get(estado, (0.0, 0.0))
    return round(random.uniform(speed_range[0], speed_range[1]), 2)


def simulate_heart_rate(estado, current_speed):
    base_bpm = 60

    bpm_multipliers = {
        "adormir": 0.9,
        "adescansar": 1.0,
        "acorrer": 1.8
    }

    multiplier = bpm_multipliers.get(estado, 1.0)
    speed_factor = current_speed / 20
    variation = random.uniform(-0.03, 0.03)
    final_bpm = base_bpm * multiplier * (1 + speed_factor) * (1 + variation)
    final_bpm = min(final_bpm, 130)

    return round(final_bpm, 1)


def simulate_respiratory_rate(estado, current_speed, bpm):
    base_respirations = 12

    respiratory_multipliers = {
        "adormir": 0.8,
        "adescansar": 1.0,
        "acorrer": 2.0
    }

    multiplier = respiratory_multipliers.get(estado, 1.0)
    speed_factor = current_speed / 25
    bpm_factor = (bpm - 60) / 70

    variation = random.uniform(-0.05, 0.05)
    final_rate = (
            base_respirations *
            multiplier *
            (1 + speed_factor * 0.5) *
            (1 + bpm_factor * 0.3) *
            (1 + variation)
    )

    final_rate = min(final_rate, 50)

    return round(final_rate, 1)


def simulate_location(last_location, current_speed, update_interval, estado):
    if last_location is None:
        return {
            "latitude": 38.722252,
            "longitude": -9.139337
        }

    speed_deg = (current_speed / 3600) / 111

    if estado == "adormir":
        movement_factor = 0
    elif estado == "adescansar":
        movement_factor = 0.3
    else:
        movement_factor = 1.0

    displacement = speed_deg * update_interval * movement_factor

    if estado == "acorrer":
        direction_change = random.uniform(-30, 30)
    else:
        direction_change = random.uniform(-90, 90)

    new_latitude = last_location["latitude"] + displacement * random.uniform(-1, 1)
    new_longitude = last_location["longitude"] + displacement * random.uniform(-1, 1)

    return {
        "latitude": round(new_latitude, 6),
        "longitude": round(new_longitude, 6)
    }


def calculate_transition_speed(current_speed, target_speed, transition_progress):
    return current_speed + (target_speed - current_speed) * transition_progress


blinking = {}

def create_kafka_consumer(device_id):
    consumer_config = {
        'bootstrap.servers': 'localhost:9092',
        'group.id': f'animal-simulation-consumer-{device_id}',
        'enable.auto.commit': True,
        'auto.offset.reset': 'earliest'
    }
    
    consumer = Consumer(consumer_config)
    consumer.subscribe(['action'])
    return consumer

async def consume_messages(device_id):

    blinking[device_id] = False
        
    consumer = create_kafka_consumer(device_id)
    logger[device_id].info("Consumer created")
    
    try:
        while True:
            await asyncio.sleep(0.5)

            # Aguarda indefinidamente até que uma mensagem seja recebida
            logger[device_id].debug("Waiting for messages")
            msg_lst = consumer.consume(timeout=0.3)
            logger[device_id].debug(f"Received {len(msg_lst)} messages")
            
            for msg in msg_lst:
                logger[device_id].debug(f"Received message: key={msg.key()}, value={msg.value()}")
                if msg is None:
                    continue  # Nenhuma mensagem ainda, continua aguardando
                
                if msg.error():
                    if msg.error().code() == KafkaError._PARTITION_EOF:
                        # Fim da partição, mensagem informativa
                        logger[device_id].debug(f"End of partition reached {msg.partition()}")
                    elif msg.error().code() == KafkaError.UNKNOWN_TOPIC_OR_PART:
                        logger[device_id].debug(f"Topic {msg.topic()} does not exist")
                    elif msg.error():
                        raise KafkaException(msg.error())
                    continue
                
                # Processa mensagem válida
                message_key = msg.key().decode('utf-8') if msg.key() else None
                if message_key == str(device_id):
                    message_value = json.loads(msg.value().decode('utf-8'))
                    action = message_value['actionType']
                    if action == 'Blink':
                        if blinking[device_id]:
                            logger[device_id].info("STOP blinking")
                            blinking[device_id] = False
                        else:
                            logger[device_id].info("START blinking")
                            blinking[device_id] = True
                    elif action == 'Sound':
                        logger[device_id].info("Playing sound")
                    else:
                        logger[device_id].warning(f"Unknown action: {action}")
                    
    except KeyboardInterrupt:
        logger[device_id].info("Consumer interrupted by user")
    finally:
        consumer.close()
                        
async def simulate_device(device_id, animal_id, initial_battery):
    print_qrcode(device_id)
    blinking[device_id] = False

    last_state = "adescansar"
    update_interval = 3
    last_location = None
    tracking_data = []

    producer = create_kafka_producer()
    topic = "animal_tracking_topic"

    last_state = "adescansar"
    update_interval = 2
    last_location = None
    last_battery = float(initial_battery)

    state_duration = {
        'adormir': random.randint(20, 40),
        'acorrer': random.randint(8, 15),
        'adescansar': random.randint(10, 20)
    }
    transition_period = 4
    current_state_time = 0
    current_speed = simulate_animal_speed(last_state)
    next_state = None

    if last_battery > 20.0:
        state = 'discharging'
    else:
        state = 'charging'

    battery_timer = 0

    while True:
        try:
            start_time = time.time()

            battery_timer += update_interval

            if battery_timer >= 10:
                if state == 'discharging':
                    last_battery -= 1.0
                    logger[device_id].debug(f"Dispositivo {device_id}: Battery reduced to {last_battery}%")
                    if last_battery <= 20.0:
                        state = 'charging'
                        logger[device_id].debug(f"Dispositivo {device_id}: Battery state changed to 'charging'")
                elif state == 'charging':
                    if last_battery < 100.0:
                        last_battery += 1.0
                        logger[device_id].debug(f"Dispositivo {device_id}: Battery increased to {last_battery}%")
                        if last_battery >= 100.0:
                            state = 'discharging'
                            logger[device_id].debug(f"Dispositivo {device_id}: Battery full. Changing battery state to 'discharging'")
                    else:
                        state = 'discharging'
                        logger[device_id].debug(f"Dispositivo {device_id}: Battery is already at 100%. Changing battery state to 'discharging'")

                last_battery = max(0.0, min(100.0, last_battery))

                battery_timer = 0

            current_state_time += 1
            time_until_change = state_duration[last_state] - current_state_time

            if time_until_change == transition_period and next_state is None:
                _, state_data = simulate_state_data(device_id, last_state)
                next_state = state_data['estado']

            if current_state_time >= state_duration[last_state]:
                if next_state:
                    last_state = next_state
                    current_state_time = 0
                    state_duration[last_state] = random.randint(
                        8 if last_state == 'acorrer' else 10,
                        15 if last_state == 'acorrer' else 40
                    )
                    next_state = None

            target_speed = simulate_animal_speed(last_state)

            if next_state and time_until_change <= transition_period:
                next_state_speed = simulate_animal_speed(next_state)
                transition_progress = (transition_period - time_until_change) / transition_period
                current_speed = calculate_transition_speed(current_speed, next_state_speed, transition_progress)
            else:
                current_speed = calculate_transition_speed(current_speed, target_speed, 0.3)

            bpm = simulate_heart_rate(last_state, current_speed)
            last_location = simulate_location(last_location, current_speed, update_interval, last_state)
            respiratory_rate = simulate_respiratory_rate(last_state, current_speed, bpm)

            data = {
                'device_id': device_id,
                'speed': current_speed,
                'bpm': bpm,
                'respiratory_rate': respiratory_rate,
                'latitude': last_location['latitude'],
                'longitude': last_location['longitude'],
                'blinking': blinking[device_id],
                'batteryPercentage': round(last_battery, 2),
                'timestamp': int(time.time())
            }

            
            send_to_kafka(producer, topic, device_id, json.dumps(data))
            logger[device_id].debug(f"Sended data: {data}")

            await asyncio.sleep(update_interval)

            elapsed_time = time.time() - start_time

            logger[device_id].debug(f"Elapsed time: {elapsed_time}")

        except Exception as e:
            logger[device_id].error(f"Error simulating device: {e}\n{traceback.format_exc()}")
            await asyncio.sleep(5)


async def run_simulation_for_device(device_id):
    animal_id = get_animal_id(device_id)
    if animal_id is None:
        logger[device_id].error(f"Was not possible to retrieve animal id")
        return

    last_battery = get_last_battery_level(animal_id, device_id)
    if last_battery is None:
        logger[device_id].warning(f"Was not possible to retrieve the battery level for the animal ID {animal_id}.")
        last_battery = 100.0

    await simulate_device(device_id, animal_id, last_battery)    

async def main(device_id):
    await asyncio.gather(run_simulation_for_device(device_id), consume_messages(device_id))

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python producer.py <device_id>")
        sys.exit(1)
    device_id = sys.argv[1]
    logger[device_id] = create_device_logger(device_id)
    asyncio.run(main(device_id))







