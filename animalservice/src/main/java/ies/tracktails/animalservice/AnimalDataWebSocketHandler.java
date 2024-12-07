package ies.tracktails.animalservice;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.TextMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import ies.tracktails.animalsDataCore.services.AnimalService;
import ies.tracktails.animalservice.dtos.SessionInfo;
import ies.tracktails.animalservice.dtos.SubscribeRequestDTO;

@Component
public class AnimalDataWebSocketHandler extends TextWebSocketHandler {
    private final AnimalService animalService;

    AnimalDataWebSocketHandler(AnimalService animalService) {
        this.animalService = animalService;
    }

    // WebSocketSession -> SessionInfo hash index
    private final ConcurrentHashMap<WebSocketSession, SessionInfo> sessions = new ConcurrentHashMap<>();
    // AnimalId -> Set<WebSocketSession> hash index
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<SessionInfo>> subscriptions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = session.getHandshakeHeaders().getFirst("X-User-Id");
        if (userId == null) {
            System.out.println("User ID não encontrado no cabeçalho!");
            session.close();
            return;
        }

        System.out.println("WebSocket " + session.getId() + " estabelecido para User ID: " + userId);

        sessions.put(session, new SessionInfo(session, userId));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.printf("Received message: %s from session: %s%n", payload, session.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        SubscribeRequestDTO subscribeRequest = objectMapper.readValue(payload, SubscribeRequestDTO.class);

        if (!"subscribe".equals(subscribeRequest.getAction())) {
            System.out.println("Invalid action in message.");
            return;
        }

        String animalId = subscribeRequest.getAnimalId();
        String userId = session.getHandshakeHeaders().getFirst("X-User-Id");

        boolean hasAccess = animalService.userHasAccessToAnimal(userId, animalId);
        if (!hasAccess) {
            System.out.printf("User %s does not have access to animal %s%n", userId, animalId);
            return;
        }

        sessions.get(session).subscribeAnimal(animalId);
        // add subscription to hash index
        subscriptions.computeIfAbsent(animalId, k -> new CopyOnWriteArraySet<>()).add(sessions.get(session));
        System.out.printf("Session %s subscribed to animal %s%n", session.getId(), animalId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status)
            throws Exception {
        for (String subscribedAnimalId : sessions.get(session).getSubscribedAnimals()) {
            subscriptions.get(subscribedAnimalId).remove(sessions.get(session));
        }
        sessions.remove(session);
        System.out.println("WebSocket session closed: " + session.getId());
    }

    public void broadcast(String animalId, String data) {
        if (!subscriptions.containsKey(animalId)) {
            return;
        }
        for (SessionInfo sessionInfo : subscriptions.get(animalId)) {
            WebSocketSession session = sessionInfo.getSession();
            System.out.println("Broadcasting data " + data + " to session " + session.getId());
            try {
                session.sendMessage(new TextMessage(data));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}