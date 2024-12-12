import asyncio
import mariadb
import os
import sys
import dotenv
from asyncio.subprocess import create_subprocess_exec
import aiomysql
import logging

dotenv.load_dotenv()

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

async def run_simulation_for_device(device_id):
    device_id_str = str(device_id)
    logging.info(f"Iniciando simulação para o dispositivo: {device_id_str}")

    try:
        process = await create_subprocess_exec(
            'python3', 'producer.py', device_id_str
        )
        await process.communicate()
    except Exception as e:
        logging.error(f"Error running simulation for device {device_id}: {e}")

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



