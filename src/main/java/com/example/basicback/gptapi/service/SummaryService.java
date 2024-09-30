package com.example.basicback.gptapi.service;

import com.example.basicback.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SummaryService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ChatGptService chatGptService;

    public Mono<String> summarizeReports() {
        // 모든 Report의 content를 가져와 ChatGPT에게 요약 요청
        String content = reportService.getConcatenatedContent();
        return chatGptService.summarizeContent(content);
    }
}