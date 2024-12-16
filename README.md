# TrackTails

## What is Tracktails
TrackTails is an innovative application that will allow pet owners to get some useful information about their pets, specifically dogs and cats. The application would make data like location, vital signs, sleep patterns and escape alerts available to pet owners easily.

## Team
|Número Mecanográfico|Nome|Role|
|------|------------------|-------------|
|114624|Sebastião Teixeira|Team Manager|
|114614|Martim Santos|Product Owner|
|114192|Diogo Domingues|Architect|
|113786|Gabriel Silva|DevOps|


## Installation and Usage
To install and run main application, you need to have Docker installed on your machine. After that, you can run the following commands:
```bash
docker compose -f docker-compose.dev.yml up # for dev environment
docker compose -f docker-compose.yml up # for production environment
```
These will require a .env file, of which you can find an example in example.env.

Docker will only run the application, it will not generate any animal data. To generate data you need to install the following dependencies:
```bash
sudo apt install python3-poetry # or equivalent for your OS
sudo apt install libmariadb3 libmariadb-dev
```

After installing the dependencies, you can run the following commands to generate data in directory `datasource`:
```bash
poetry install
poetry run python datasource/main.py
```