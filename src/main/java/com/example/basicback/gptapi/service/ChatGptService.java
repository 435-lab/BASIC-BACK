package com.example.basicback.gptapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ChatGptService {
    private final WebClient webClient;

    @Value("${chatgpt-apikey}")
    private String apiKey;

    public ChatGptService(WebClient.Builder webClientBuilder) {
        // OpenAI API URL로 설정
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
    }

    public Mono<String> summarizeContent(String content) {
        String prompt = "다음 게시글 내용을 요약하세요:\n\n" + content;

        return webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(buildRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error occurred: " + e.getMessage()));
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        return Map.of(
                "model", "gpt-3.5-turbo",
                "messages", new Object[]{
                        Map.of("role", "user", "content", prompt)
                }
        );
    }
}
