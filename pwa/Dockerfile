# Stage 1: Build the application
FROM node:22.11.0 AS build
 
WORKDIR /app
 
# Install dependencies
COPY ./pwa/package.json ./pwa/package-lock.json ./
RUN npm install --production

# Copy the source code and build the app
COPY ./pwa ./
RUN npm run build

# Stage 1: Build the application
FROM node:22.11.0 AS build

WORKDIR /app

# Install dependencies
COPY ./pwa/package.json ./pwa/package-lock.json ./
RUN npm install --production

# Copy the source code and build the app
COPY ./pwa ./
RUN npm run build

# Stage 2: Serve the built application
FROM node:22.11.0

WORKDIR /app

# Install a lightweight HTTP server for serving static files
RUN npm install -g serve

# Copy the built files from the previous stage
COPY --from=build /app/dist ./dist

# Serve the static files
CMD ["serve", "-s", "dist", "-l", "5000"]