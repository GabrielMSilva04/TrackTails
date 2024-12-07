package ies.tracktails.animalservice;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.TextMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnimalDataWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<WebSocketSession, String> sessionSubscriptions = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket session established: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.printf("Received message: %s from session: %s", payload, session.getId());

        // Parse the incoming message (assume JSON for simplicity)
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload);

        if (jsonNode.has("action") && "subscribe".equals(jsonNode.get("action").asText()) && jsonNode.has("animalId")) {
            String animalId = jsonNode.get("animalId").asText();
            sessionSubscriptions.put(session, animalId);
            System.out.printf("Session %s subscribed to animal %s", session.getId(), animalId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessionSubscriptions.remove(session);
        System.out.println("WebSocket session closed: " + session.getId());
    }

    public void broadcast(String animalId, String data) {
        for (Map.Entry<WebSocketSession, String> entry : sessionSubscriptions.entrySet()) {
            WebSocketSession session = entry.getKey();
            String subscribedAnimalId = entry.getValue();
            if (subscribedAnimalId.equals(animalId)) {
                System.out.println("Broadcasting data " + data + " to session " + session.getId());
                try {
                    session.sendMessage(new TextMessage(String.format("{\"animalId\": \"%s\", \"data\": %s}", animalId, data)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}