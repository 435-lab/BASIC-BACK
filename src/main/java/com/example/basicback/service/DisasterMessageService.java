package com.example.basicback.service;

import com.example.basicback.model.DisasterMessage;
import com.example.basicback.repository.DisasterMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisasterMessageService {

    private final DisasterMessageRepository repository;

    public void saveDisasterMessage(DisasterMessage message) {
        if (!repository.existsByMd101Sn(message.getMd101Sn())) {
            repository.save(message);
        }
    }

    public List<DisasterMessage> getMessagesByLocation(String locationName) {
        return repository.findByLocationName(locationName);
    }
}
