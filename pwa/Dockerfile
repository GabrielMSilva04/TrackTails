# Stage 1: Build the application
FROM node:22.11.0 AS build

WORKDIR /app

# Copy package files and install all dependencies, including devDependencies
COPY ./pwa/package.json ./pwa/package-lock.json ./
RUN npm install

# Copy the source code
COPY ./pwa ./

# Build the application using `vite`
RUN npm run build

# Stage 2: Serve the built application
FROM node:22.11.0

WORKDIR /app

# Install a lightweight HTTP server for serving static files
RUN npm install -g serve

# Copy the built files from the previous stage
COPY --from=build /app/dist ./dist

# Set environment variables for the server
ENV NODE_ENV=production

# Serve the static files
CMD ["serve", "-s", "dist"]
