package com.example.basicback.gptapi.controller;

import com.example.basicback.gptapi.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping("/summarize")
    public Mono<String> getSummary() {
        return summaryService.summarizeReports();
    }
}