package com.example.basicback.gptapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ChatGptService {

    private final WebClient webClient;

    @Value("${chatgpt.apikey}")
    private String apiKey;

    public ChatGptService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
    }

    public Mono<String> summarizeContent(String content) {
        String prompt = "당신은 재난 제보게시판에 올라온 게시글을 요약하여 짧게 만드는 역할이야. 그리고 문장은 5개 생성하고 각 문장뒤에 줄바꿈 해줘" + content;

        return webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(buildRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    // 에러 발생 시 처리
                    return Mono.just("Error occurred: " + e.getMessage());
                });
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