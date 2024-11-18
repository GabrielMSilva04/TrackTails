FROM node:22 AS build
 
WORKDIR /app
 
 
COPY ./pwa/package.json ./pwa/package-lock.json ./
RUN npm install

COPY ./pwa ./

RUN npm run build

EXPOSE 8080

CMD [ "npm", "run", "dev" ]