package com.example.basicback.scheduler;

import com.example.basicback.dto.DisasterApiResponse;
import com.example.basicback.model.DisasterMessage;
import com.example.basicback.service.DisasterMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataFetchScheduler {

    private final DisasterMessageService service;
    private final RestTemplate restTemplate;

    @Value("${disaster.api.url}")
    private String apiUrl;

    @Value("${disaster.api.key}")
    private String apiKey;

    @Scheduled(fixedRate = 60000) // Fetch data every minute
    public void fetchDisasterMessages() {
        try {
            String encodedServiceKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString());

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("serviceKey", encodedServiceKey)
                    .queryParam("type", "xml")
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", "10")
                    .queryParam("flag", "Y");

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_XML));

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<DisasterApiResponse> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    DisasterApiResponse.class
            );

            log.info("API Response: {}", response.getBody());

            if (response.getBody() != null && response.getBody().getRow() != null) {
                for (DisasterApiResponse.Row row : response.getBody().getRow()) {
                    DisasterMessage message = new DisasterMessage();
                    message.setLocationName(row.getLocationName());
                    message.setMessage(row.getMsg());
                    message.setMd101Sn(row.getMd101Sn());
                    message.setCreateDate(LocalDateTime.parse(row.getCreateDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    service.saveDisasterMessage(message);
                }
            }
        } catch (Exception e) {
            log.error("Error fetching disaster messages", e);
        }
    }
}