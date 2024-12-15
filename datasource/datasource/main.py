import asyncio
import aiomysql
import logging
import dotenv
import os
import sys
from device import run_simulation_for_device, consume_messages, logger, create_device_logger

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


async def main():
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(levelname)s - %(message)s'
    )

    try:
        device_ids = await get_device_ids_from_db()
        for device_id in device_ids:
            logger[device_id] = create_device_logger(device_id)
        tasks = [run_simulation_for_device(device_id) for device_id in device_ids]
        tasks += [consume_messages(device_id) for device_id in device_ids]
        await asyncio.gather(*tasks)

    except KeyboardInterrupt:
        logging.info("Simulation stopped by user")
    except Exception as e:
        logging.error(f"An error occurred: {e}")
    finally:
        for task in asyncio.all_tasks():
            if not task.done():
                task.cancel()


if __name__ == "__main__":
    asyncio.run(main())
