package com.example.basicback.scheduler;

import com.example.basicback.model.DisasterMessage;
import com.example.basicback.service.DisasterMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataFetchScheduler {

    private final DisasterMessageService service;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${disaster.api.url}")
    private String apiUrl;

    @Value("${disaster.api.key}")
    private String apiKey;

    @Autowired
    public DataFetchScheduler(DisasterMessageService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Scheduled(fixedRateString = "${disaster.api.fetchInterval}")
    public void fetchDisasterMessages() {
        log.info("Starting fetchDisasterMessages scheduled task");

        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("serviceKey", apiKey)
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", "10")
                    .queryParam("type", "xml")
                    .build()
                    .toUriString();

            log.info("API Request URL: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String xmlResponse = response.getBody();
                log.debug("Full XML Response: {}", xmlResponse);

                JSONObject jsonResponse = XML.toJSONObject(xmlResponse);
                log.info("Converted JSON Response: {}", jsonResponse.toString(2)); // JSON 구조를 보기 쉽게 출력

                processJsonResponse(jsonResponse);
            } else {
                log.error("API request failed with status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error fetching disaster messages", e);
        }
    }

    private void processJsonResponse(JSONObject jsonResponse) {
        try {
            // JSON 구조를 로그로 출력
            log.info("JSON Response Structure: {}", jsonResponse.toString(2));

            // 실제 JSON 구조에 맞게 파싱 로직 수정
            if (jsonResponse.has("DisasterMsg")) {
                JSONObject disasterMsg = jsonResponse.getJSONObject("DisasterMsg");
                processDisasterMsg(disasterMsg);
            } else if (jsonResponse.has("row")) {
                processRows(jsonResponse.getJSONArray("row"));
            } else {
                log.error("Unexpected JSON structure");
            }
        } catch (Exception e) {
            log.error("Error processing JSON response", e);
        }
    }

    private void processDisasterMsg(JSONObject disasterMsg) {
        if (disasterMsg.has("head")) {
            JSONObject head = disasterMsg.getJSONObject("head");
            String resultCode = head.getJSONObject("RESULT").getString("resultCode");

            if (!"INFO-0".equals(resultCode)) {
                String resultMsg = head.getJSONObject("RESULT").getString("resultMsg");
                log.error("API returned an error. Code: {}, Message: {}", resultCode, resultMsg);
                return;
            }
        }

        if (disasterMsg.has("row")) {
            processRows(disasterMsg.getJSONArray("row"));
        }
    }

    private void processRows(JSONArray rows) {
        List<DisasterMessage> messages = new ArrayList<>();

        for (int i = 0; i < rows.length(); i++) {
            try {
                DisasterMessage message = objectMapper.readValue(rows.getJSONObject(i).toString(), DisasterMessage.class);
                messages.add(message);
            } catch (Exception e) {
                log.error("Error parsing disaster message", e);
            }
        }

        log.info("Parsed {} disaster messages", messages.size());
        for (DisasterMessage message : messages) {
            service.saveDisasterMessage(message);
        }
    }
}
