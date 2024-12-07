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

        String action = subscribeRequest.getAction();
        String animalId = subscribeRequest.getAnimalId();
        String userId = session.getHandshakeHeaders().getFirst("X-User-Id");

        switch (action) {
            case "subscribe":
                handleSubscribe(session, animalId, userId);
                break;
            case "unsubscribe":
                handleUnsubscribe(session, animalId);
                break;
            default:
                System.out.println("Invalid action: " + action);
        }
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

    private void handleSubscribe(WebSocketSession session, String animalId, String userId) {
        boolean hasAccess = animalService.userHasAccessToAnimal(userId, animalId);
        if (!hasAccess) {
            System.out.printf("User %s does not have access to animal %s%n", userId, animalId);
            return;
        }
    
        sessions.get(session).subscribeAnimal(animalId);
        // Add session to animal subscriptions index
        subscriptions.computeIfAbsent(animalId, k -> new CopyOnWriteArraySet<>()).add(sessions.get(session));
        System.out.printf("Session %s subscribed to animal %s%n", session.getId(), animalId);
    }

    private void handleUnsubscribe(WebSocketSession session, String animalId) {
        SessionInfo sessionInfo = sessions.get(session);
    
        if (sessionInfo == null || !sessionInfo.getSubscribedAnimals().contains(animalId)) {
            System.out.printf("Session %s is not subscribed to animal %s%n", session.getId(), animalId);
            return;
        }
    
        sessionInfo.unsubscribeAnimal(animalId);
    
        // Remove session from animal subscriptions index
        CopyOnWriteArraySet<SessionInfo> animalSubscribers = subscriptions.get(animalId);
        if (animalSubscribers != null) {
            animalSubscribers.remove(sessionInfo);
            if (animalSubscribers.isEmpty()) {
                subscriptions.remove(animalId);
            }
        }
        System.out.printf("Session %s unsubscribed from animal %s%n", session.getId(), animalId);
    }    
}