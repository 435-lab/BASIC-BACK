package com.example.basicback.missingfetcher;

import com.example.basicback.disasterfetcher.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MissingMessageController {

    @Autowired
    private MissingScheduler missingscheduler;

    @Autowired
    private GeocodingService geocodingService;

    @GetMapping("/missing-message")
    public ResponseEntity<List<MissingMessage>> getDisasterMessages(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        String region = geocodingService.getRegionFromCoordinates(latitude, longitude);
        List<MissingMessage> messages = missingscheduler.getMissingMessagesForLocation(region);

        return ResponseEntity.ok(messages);
    }
}