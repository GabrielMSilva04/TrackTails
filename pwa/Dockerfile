# Stage 1: Build the application
FROM node:22.11.0 AS build
 
WORKDIR /app
 
# Install dependencies
COPY ./pwa/package.json ./pwa/package-lock.json ./
RUN npm install --production

# Copy the source code and build the app
COPY ./pwa ./
RUN npm run build

EXPOSE 8080

CMD ["npm", "start"]
