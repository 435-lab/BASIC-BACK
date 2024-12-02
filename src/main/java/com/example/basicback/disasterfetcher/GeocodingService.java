package com.example.basicback.disasterfetcher;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class GeocodingService {
    private final String KAKAO_API_KEY = "09b81b81d38105c5d39dac64b7849bb9";
    private final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    public String getRegionFromCoordinates(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = KAKAO_API_URL + "?x=" + longitude + "&y=" + latitude;

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>("parameters", headers);

        org.springframework.http.ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(
                url, org.springframework.http.HttpMethod.GET, entity, KakaoApiResponse.class);

        if (response.getBody() != null && !response.getBody().getDocuments().isEmpty()) {
            return response.getBody().getDocuments().get(0).getRegion_1depth_name();
        }
        return "Unknown";
    }
}

class KakaoApiResponse {
    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    static class Document {
        private String region_1depth_name;

        public String getRegion_1depth_name() {
            return region_1depth_name;
        }

        public void setRegion_1depth_name(String region_1depth_name) {
            this.region_1depth_name = region_1depth_name;
        }
    }
}