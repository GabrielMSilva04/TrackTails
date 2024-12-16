package ies.tracktails.datacollector.configurations;

import ies.tracktails.animalsDataCore.services.AnimalDataService;
import ies.tracktails.datacollector.WebSocketNotificationListener;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AnimalDataConfig {
    public AnimalDataConfig(AnimalDataService eventService, WebSocketNotificationListener webSocketListener) {
        System.out.println("Adding WebSocketNotificationListener to event service");
        eventService.addListener(webSocketListener);
    }
}