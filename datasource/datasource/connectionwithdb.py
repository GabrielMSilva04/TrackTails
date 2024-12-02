import mariadb
import os
import sys
import dotenv
import time
import multiprocessing
from subprocess import Popen

dotenv.load_dotenv()

def get_device_ids_from_db():
    try:
        conn = mariadb.connect(
            user=os.getenv("DB_USER"),
            password=os.getenv("DB_PASSWORD"),
            host="127.0.0.1",  # Usando 127.0.0.1 para conectar ao contêiner
            port=3306,          # A porta exposta pelo Docker
            database=os.getenv("DB_NAME")
        )


        cursor = conn.cursor()
        cursor.execute("SELECT device_id FROM animals")
        device_ids = [device_id[0] for device_id in cursor]

        cursor.close()
        conn.close()

        return device_ids

    except mariadb.Error as e:
        print(f"Error connecting to MariaDB Platform: {e}")
        sys.exit(1)

def run_simulation_for_device(device_id):
    device_id_str = str(device_id)

    print(f"Iniciando simulação para o dispositivo: {device_id_str}")

    process = Popen(['python3', 'producer.py', device_id_str])
    process.communicate()

def main():

    device_ids = get_device_ids_from_db()


    processes = []

    for device_id in device_ids:

        p = multiprocessing.Process(target=run_simulation_for_device, args=(device_id,))
        processes.append(p)
        p.start()


    try:
        while True:

            time.sleep(1)
    except KeyboardInterrupt:
        print("Simulação interrompida manualmente")
        for p in processes:
            p.terminate()

if __name__ == "__main__":
    main()



