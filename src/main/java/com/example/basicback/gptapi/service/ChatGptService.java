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
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
    }

    public Mono<String> summarizeContent(String content) {
        String prompt = "다음내용을 요약해주세요. 비슷한 내용은 한문장으로 요약하고 다른내용이다 싶으면 다른 문장으로 써주세요. 각 내용마다 다른 문장으로 쓰고 앞에 번호를 붙혀주세요." + content;

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
                "model", "gpt-4",
                "messages", new Object[]{
                        Map.of("role", "user", "content", prompt)
                }
        );
    }
}
