package com.example.basicback.disasterfetcher;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class DisasterFetcher {

    private static final Logger log = Logger.getLogger(DisasterFetcher.class.getName());
    private String apiKey = "";  // Replace with your API key
    private String apiUrl = "";  // Replace with your API URL

    public List<DisasterMessage> fetchDisasterMessages() {
        List<DisasterMessage> messages = new ArrayList<>();
        log.info("Starting fetchDisasterMessages scheduled task");
        try {
            String encodedServiceKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString());
            log.info("Encoded service key: " + encodedServiceKey);

            String url = apiUrl + "?serviceKey=" + encodedServiceKey + "&type=xml&pageNo=1&numOfRows=10&flag=Y";
            log.info("API Request URL: " + url);

            URL apiEndpoint = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiEndpoint.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");

            log.info("Sending request to API");
            int responseCode = connection.getResponseCode();
            log.info("API Response Status: " + responseCode);

            if (responseCode == 200) {
                Scanner scanner = new Scanner(apiEndpoint.openStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                log.info("Response: " + response);

                // XML 파싱을 수행합니다.
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new java.io.ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8)));

                NodeList rowNodes = doc.getElementsByTagName("row");

                for (int i = 0; i < rowNodes.getLength(); i++) {
                    Element rowElement = (Element) rowNodes.item(i);

                    String createDateStr = rowElement.getElementsByTagName("create_date").item(0).getTextContent();
                    String locationName = rowElement.getElementsByTagName("location_name").item(0).getTextContent();
                    String md101Sn = rowElement.getElementsByTagName("md101_sn").item(0).getTextContent();
                    String msg = rowElement.getElementsByTagName("msg").item(0).getTextContent();

                    DisasterMessage message = new DisasterMessage();
                    message.setLocationName(locationName);
                    message.setMessage(msg);
                    message.setMd101Sn(md101Sn);
                    message.setCreateDate(LocalDateTime.parse(createDateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

                    log.info("Parsed disaster message: locationName=" + message.getLocationName()
                            + ", md101Sn=" + message.getMd101Sn() + ", createDate=" + message.getCreateDate());

                    messages.add(message);
                }
            } else {
                log.warning("No disaster messages found in the API response");
            }
        } catch (Exception e) {
            log.severe("Error fetching disaster messages: " + e.getMessage());
            e.printStackTrace();
        }
        log.info("Completed fetchDisasterMessages scheduled task");
        return messages;
    }
}