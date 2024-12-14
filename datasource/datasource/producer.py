import asyncio
import mariadb
import os
import sys
import dotenv
import aiomysql
import logging
import requests
from influxdb_client import InfluxDBClient
from influxdb_client.client.query_api import QueryApi
import json
import random
import time
from confluent_kafka import Producer
from datetime import datetime

dotenv.load_dotenv()

INFLUXDB_URL = f"http://localhost:{os.getenv('INFLUXDB_PORT')}"
INFLUXDB_TOKEN = os.getenv('DOCKER_INFLUXDB_INIT_ADMIN_TOKEN')
INFLUXDB_ORG = os.getenv('DOCKER_INFLUXDB_INIT_ORG')
INFLUXDB_BUCKET = os.getenv('DOCKER_INFLUXDB_INIT_BUCKET')


def get_last_battery_level(animal_id):
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
        print(f"Error querying InfluxDB for animal ID {animal_id}: {e}")
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


def send_to_kafka(producer, topic, message):
    try:
        producer.produce(topic, value=message)
        producer.flush()
    except Exception as e:
        logging.error(f"Erro ao enviar para o Kafka: {e}")


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


async def simulate_device(device_id, animal_id, initial_battery):
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
                    logging.info(f"Dispositivo {device_id}: Bateria diminuída para {last_battery}%")
                    if last_battery <= 20.0:
                        state = 'charging'
                        logging.info(f"Dispositivo {device_id}: Mudar para estado 'charging'")
                elif state == 'charging':
                    if last_battery < 100.0:
                        last_battery += 1.0
                        logging.info(f"Dispositivo {device_id}: Bateria aumentada para {last_battery}%")
                        if last_battery >= 100.0:
                            state = 'discharging'
                            logging.info(f"Dispositivo {device_id}: Bateria cheia. Mudar para estado 'discharging'")
                    else:
                        state = 'discharging'
                        logging.info(f"Dispositivo {device_id}: Bateria já está em 100%. Mudar para estado 'discharging'")

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
                'batteryPercentage': round(last_battery, 2),
                'timestamp': int(time.time())
            }

            send_to_kafka(producer, topic, json.dumps(data))

            await asyncio.sleep(update_interval)

            elapsed_time = time.time() - start_time

        except Exception as e:
            logging.error(f"Erro na simulação para o dispositivo {device_id}: {e}")
            await asyncio.sleep(5)


async def run_simulation_for_device(device_id):
    animal_id = get_animal_id(device_id)
    if animal_id is None:
        logging.error(f"Não foi possível obter o animal ID para o dispositivo {device_id}")
        return

    last_battery = get_last_battery_level(animal_id)
    if last_battery is None:
        logging.warning(f"Não foi possível obter o nível de bateria para o animal ID {animal_id}.")
        last_battery = 100.0

    await simulate_device(device_id, animal_id, last_battery)


async def get_device_ids_from_db():
    try:
        conn = await aiomysql.connect(
            user=os.getenv("DB_USER"),
            password=os.getenv("DB_PASSWORD"),
            host="127.0.0.1",
            port=3306,
            db=os.getenv("DB_NAME")
        )

        async with conn.cursor() as cursor:
            await cursor.execute("SELECT device_id FROM animals")
            device_ids = [device_id[0] for device_id in await cursor.fetchall()]

        conn.close()
        return device_ids

    except Exception as e:
        logging.error(f"Error connecting to Database: {e}")
        sys.exit(1)


async def main():
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(levelname)s - %(message)s'
    )

    try:
        device_ids = await get_device_ids_from_db()
        tasks = [run_simulation_for_device(device_id) for device_id in device_ids]
        await asyncio.gather(*tasks)

    except KeyboardInterrupt:
        logging.info("Simulação interrompida manualmente")
    except Exception as e:
        logging.error(f"An error occurred: {e}")
    finally:
        for task in asyncio.all_tasks():
            if not task.done():
                task.cancel()


if __name__ == "__main__":
    asyncio.run(main())







