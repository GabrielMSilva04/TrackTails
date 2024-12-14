package ies.tracktails.animalservice.dtos;

import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.HashSet;

public class SessionInfo {
    private WebSocketSession session;
    private String userId;
    private Set<String> subscribedAnimals;

    public SessionInfo(WebSocketSession session, String userId) {
        this.session = session;
        this.userId = userId;
        this.subscribedAnimals = new HashSet<>();
    }

    // Getters and setters
    public WebSocketSession getSession() {
        return session;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void subscribeAnimal(String animalId) {
        if (!subscribedAnimals.contains(animalId)) {
            subscribedAnimals.add(animalId);
        }
    }

    public void unsubscribeAnimal(String animalId) {
        subscribedAnimals.remove(animalId);
    }

    public boolean isSubscribedToAnimal(String animalId) {
        return subscribedAnimals.contains(animalId);
    }

    public Set<String> getSubscribedAnimals() {
        return subscribedAnimals;
    }
}
