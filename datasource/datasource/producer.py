import json
import random
import time
import folium
import matplotlib.pyplot as plt
from datetime import datetime
import numpy as np
from confluent_kafka import Producer

def create_kafka_producer():

    conf = {
        'bootstrap.servers': 'localhost:29092',
        'client.id': 'animal-simulation-producer'
    }
    producer = Producer(conf)
    return producer

def send_to_kafka(producer, topic, message):

    try:
        producer.produce(topic, value=message)
        producer.flush()
        print(f"Mensagem enviada para o tópico {topic}: {message}")
    except Exception as e:
        print(f"Erro ao enviar mensagem para o Kafka: {e}")

def simulate_state_data(animal_id, last_state):

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
        'animal_id': animal_id,
        'estado': animal_state,
        'timestamp': int(time.time())
    }

def simulate_animal_speed(estado, animal_type="default"):

    speed_ranges = {
        "default": {
            "acorrer": (15.0, 25.0),
            "adescansar": (0.5, 3.0),
            "adormir": (0.0, 0.0)
        },
        "rapido": {
            "acorrer": (30.0, 45.0),
            "adescansar": (1.0, 5.0),
            "adormir": (0.0, 0.0)
        },
        "lento": {
            "acorrer": (8.0, 15.0),
            "adescansar": (0.2, 2.0),
            "adormir": (0.0, 0.0)
        }
    }

    animal_speeds = speed_ranges.get(animal_type, speed_ranges["default"])
    speed_range = animal_speeds.get(estado, (0.0, 0.0))
    speed = round(random.uniform(speed_range[0], speed_range[1]), 2)
    return speed

def simulate_heart_rate(estado, current_speed, animal_type="default"):

    base_bpm = {
        "default": 65,
        "rapido": 80,
        "lento": 50
    }

    bpm_multipliers = {
        "adormir": 0.8,
        "adescansar": 1.0,
        "acorrer": 1.5
    }

    base = base_bpm.get(animal_type, base_bpm["default"])
    multiplier = bpm_multipliers.get(estado, 1.0)
    speed_factor = current_speed / 10
    variation = random.uniform(-0.05, 0.05)
    final_bpm = base * multiplier * (1 + speed_factor) * (1 + variation)
    return round(final_bpm, 1)

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

def main():
    animal_id = "A001"
    animal_type = "default"
    last_state = "adescansar"
    update_interval = 5
    last_location = None
    tracking_data = []
    topic = "animal_tracking_topic"

    producer = create_kafka_producer()

    state_duration = {
        'adormir': random.randint(20, 40),
        'acorrer': random.randint(8, 15),
        'adescansar': random.randint(10, 20)
    }
    transition_period = 4
    current_state_time = 0
    current_speed = simulate_animal_speed(last_state, animal_type)
    next_state = None

    print(f"Iniciando simulação para animal {animal_id} (Tipo: {animal_type})")

    try:
        for _ in range(100):
            current_state_time += 1
            time_until_change = state_duration[last_state] - current_state_time

            if time_until_change == transition_period and next_state is None:
                _, state_data = simulate_state_data(animal_id, last_state)
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

            target_speed = simulate_animal_speed(last_state, animal_type)

            if next_state and time_until_change <= transition_period:
                next_state_speed = simulate_animal_speed(next_state, animal_type)
                transition_progress = (transition_period - time_until_change) / transition_period
                current_speed = calculate_transition_speed(current_speed, next_state_speed, transition_progress)
            else:
                current_speed = calculate_transition_speed(current_speed, target_speed, 0.3)

            bpm = simulate_heart_rate(last_state, current_speed, animal_type)
            last_location = simulate_location(last_location, current_speed, update_interval, last_state)

            data = {
                'timestamp': time.time(),
                'animal_id': animal_id,
                'state': last_state,
                'speed': current_speed,
                'bpm': bpm,
                'location': last_location
            }

            send_to_kafka(producer, topic, json.dumps(data))

            tracking_data.append(data)

            time.sleep(update_interval)

        print("\nSimulação completada!")

    except KeyboardInterrupt:
        print("\nSimulação terminada pelo usuário")
        if tracking_data:
            print("\nDados de rastreamento enviados para o Kafka.")
        print("Verifique o tópico Kafka para as mensagens enviadas.")

if __name__ == "__main__":
    main()
