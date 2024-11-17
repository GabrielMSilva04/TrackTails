FROM node:22 AS build
 
WORKDIR /app
 
 
COPY package.json package-lock.json ./
RUN npm install
 
COPY ./ ./

RUN npm run build

EXPOSE 8080
 
CMD [ "npm", "run", "dev" ]
