import { useEffect } from 'react';
import { Client } from '@stomp/stompjs';

export function useWebSocket(animalId) {
    useEffect(() => {
        const client = new Client({
            brokerURL: 'ws://localhost/ws',
            connectHeaders: {},
            heartbeatIncoming: 10000,
            heartbeatOutgoing: 10000,
            debug: (str) => console.log('WebSocket Debug:', str),
        });

        let intervalId;

        const setupClient = () => {
            client.onConnect = () => {
                console.log(`Connected to WebSocket for animal ID: ${animalId}`);

                // Subscribe to location topic
                client.subscribe(`/topic/location/${animalId}`, (message) => {
                    const updatedLocation = JSON.parse(message.body);
                    console.log('Received updated location:', updatedLocation);
                });

                // Periodically send simulated updates
                intervalId = setInterval(() => {
                    const testMessage = {
                        animalId: animalId,
                        latitude: (Math.random() * 0.01) + 40.633,
                        longitude: (Math.random() * 0.01) - 8.659,
                        timestamp: new Date().toISOString(),
                    };
                    console.log('Sending test message:', testMessage);
                    client.publish({
                        destination: `/app/location/${animalId}`,
                        body: JSON.stringify(testMessage),
                    });
                }, 5000);
            };

            client.onDisconnect = () => {
                clearInterval(intervalId);
                console.log('Disconnected from WebSocket');
            };

            client.activate();
        };

        setupClient();

        const reconnectionInterval = setInterval(() => {
            if (!client.active) {
                console.log('WebSocket is inactive. Reconnecting...');
                client.activate();
            }
        }, 10000);

        return () => {
            console.log(`Disconnecting WebSocket for animal ID: ${animalId}`);
            clearInterval(intervalId);
            clearInterval(reconnectionInterval);
            client.deactivate();
        };
    }, [animalId]);
}
