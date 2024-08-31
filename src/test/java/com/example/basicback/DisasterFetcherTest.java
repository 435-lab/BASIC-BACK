package com.example.basicback;


import com.example.basicback.disasterfetcher.DisasterFetcher;
import com.example.basicback.disasterfetcher.DisasterMessage;

import java.util.List;

public class DisasterFetcherTest {

    public static void main(String[] args) {
        DisasterFetcher fetcher = new DisasterFetcher();
        List<DisasterMessage> messages = fetcher.fetchDisasterMessages();

        // 전체 리스트를 출력합니다.
        for (DisasterMessage message : messages) {
            System.out.println("Location: " + message.getLocationName());
            System.out.println("Message: " + message.getMessage());
            System.out.println("Created Date: " + message.getCreateDate());
            System.out.println("MD101 SN: " + message.getMd101Sn());
            System.out.println("-------------------------");
        }
    }
}
