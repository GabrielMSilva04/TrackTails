# Stage 1: Build the application
FROM node:22.11.0 AS build

WORKDIR /app

COPY ./pwa/package.json ./pwa/package-lock.json ./
RUN npm install

COPY ./pwa ./

RUN npm run build

# Stage 2: Serve the built application
FROM node:22.11.0

WORKDIR /app

RUN npm install -g serve

COPY --from=build /app/dist ./dist

# Set environment variables for the server
ENV NODE_ENV=production

CMD ["serve", "-s", "dist"]
