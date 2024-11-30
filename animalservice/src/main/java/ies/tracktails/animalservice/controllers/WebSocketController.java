package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.services.AnimalDataService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Controller
public class WebSocketController {

    private final AnimalDataService animalDataService;

    // Constructor injection
    public WebSocketController(AnimalDataService animalDataService) {
        this.animalDataService = animalDataService;
    }

    @MessageMapping("/location/{animalId}")
    @SendTo("/topic/location/{animalId}")
    public AnimalDataDTO streamLocation(String animalId) {
        System.out.println("StreamLocation triggered for animalId: " + animalId);
        AnimalDataDTO liveLocation = fetchLiveLocation(animalId);
        System.out.println("Sending live location: " + liveLocation);
        return liveLocation;
    }


    private AnimalDataDTO fetchLiveLocation(String animalId) {
        // Fetch live data logic here
        System.out.println("Fetching live location data for animal: " + animalId);
        return animalDataService.getLatestValues(animalId);
    }


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/test/location/{animalId}")
    public ResponseEntity<Void> sendTestLocation(@PathVariable String animalId) {
        System.out.println("Sending test location for animal: " + animalId);
        AnimalDataDTO testData = new AnimalDataDTO(animalId);
        testData.setLatitude(40.63316);
        testData.setLongitude(-8.65939);
        testData.setTimestamp(Instant.now());
        messagingTemplate.convertAndSend("/topic/location/" + animalId, testData);
        return ResponseEntity.ok().build();
    }

}
