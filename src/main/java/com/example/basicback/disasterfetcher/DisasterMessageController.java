package com.example.basicback.disasterfetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DisasterMessageController {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private GeocodingService geocodingService;

    @GetMapping("/disaster-message")
    public ResponseEntity<List<DisasterMessage>> getDisasterMessages(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        String region = geocodingService.getRegionFromCoordinates(latitude, longitude);
        List<DisasterMessage> messages = scheduler.getDisasterMessagesForLocation(region);

        return ResponseEntity.ok(messages);
    }
}