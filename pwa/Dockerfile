# Stage 1: Build the application
FROM node:22.11.0 AS build

WORKDIR /app

# Install dependencies
COPY ./pwa/package.json ./package-lock.json ./
RUN npm install --production

# Copy source code and build the app
COPY ./pwa ./
RUN npm run build

# Stage 2: Serve the built application
FROM node:22.11.0

WORKDIR /app

RUN npm install -g serve

COPY --from=build /app/dist ./dist

ENV NODE_ENV=production

CMD ["serve", "-s", "dist", "-l", "5173"]
