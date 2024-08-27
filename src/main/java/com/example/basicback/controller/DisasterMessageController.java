package com.example.basicback.controller;

import com.example.basicback.model.DisasterMessage;
import com.example.basicback.service.DisasterMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disaster-messages")
@RequiredArgsConstructor
public class DisasterMessageController {

    private final DisasterMessageService service;

    @GetMapping("/{locationName}")
    public ResponseEntity<List<DisasterMessage>> getMessages(@PathVariable String locationName) {
        List<DisasterMessage> messages = service.getMessagesByLocation(locationName);
        return ResponseEntity.ok(messages);
    }
}
