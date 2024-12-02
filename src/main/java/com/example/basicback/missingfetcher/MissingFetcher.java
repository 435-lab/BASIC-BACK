package com.example.basicback.missingfetcher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MissingFetcher {

    private static final Logger log = Logger.getLogger(MissingFetcher.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Value("${apiKey}")
    private String serviceKey;

    @Value("${apiUrl}")
    private String apiUrl;

    public List<MissingMessage> fetchMissingMessages() {
        List<MissingMessage> messages = new ArrayList<>();
        log.info("Starting fetchDisasterMessages scheduled task");
        try {
            String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8.toString());
            String pageNo = "1";
            String numOfRows = "10";
            String returnType = "xml";
            String startDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            StringBuilder urlBuilder = new StringBuilder(apiUrl);
            urlBuilder.append("?serviceKey=").append(encodedServiceKey);
            urlBuilder.append("&pageNo=").append(pageNo);
            urlBuilder.append("&numOfRows=").append(numOfRows);
            urlBuilder.append("&returnType=").append(returnType);
            urlBuilder.append("&crtDt=").append(startDate);

            log.info("API Request URL: " + urlBuilder.toString());

            URI uri = new URI(urlBuilder.toString());
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");

            log.info("Sending request to API");
            int responseCode = connection.getResponseCode();
            log.info("API Response Status: " + responseCode);

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                log.info("Response: " + response.toString());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new java.io.ByteArrayInputStream(response.toString().getBytes(StandardCharsets.UTF_8)));

                NodeList itemNodes = doc.getElementsByTagName("item");

                for (int i = 0; i < itemNodes.getLength(); i++) {
                    Element itemElement = (Element) itemNodes.item(i);

                    MissingMessage message = new MissingMessage();
                    message.setSn(getElementContent(itemElement, "SN"));

                    try {
                        message.setCrtDt(LocalDateTime.parse(getElementContent(itemElement, "CRT_DT"), DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        log.warning("Failed to parse CRT_DT: " + getElementContent(itemElement, "CRT_DT"));
                        message.setCrtDt(null);
                    }

                    message.setMsgCn(getElementContent(itemElement, "MSG_CN"));
                    message.setRcptnRgnNm(getElementContent(itemElement, "RCPTN_RGN_NM"));
                    message.setEmrgStepNm(getElementContent(itemElement, "EMRG_STEP_NM"));
                    message.setDstSeNm(getElementContent(itemElement, "DST_SE_NM"));

                    try {
                        message.setRegYmd(LocalDateTime.parse(getElementContent(itemElement, "REG_YMD"), DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        log.warning("Failed to parse REG_YMD: " + getElementContent(itemElement, "REG_YMD"));
                        message.setRegYmd(null);
                    }

                    try {
                        message.setMdfcnYmd(LocalDateTime.parse(getElementContent(itemElement, "MDFCN_YMD"), DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        log.warning("Failed to parse MDFCN_YMD: " + getElementContent(itemElement, "MDFCN_YMD"));
                        message.setMdfcnYmd(null);
                    }

                    messages.add(message);
                }
            } else {
                log.warning("No disaster messages found in the API response");
            }
        } catch (Exception e) {
            log.severe("Error fetching disaster messages: " + e.getMessage());
        }
        log.info("Completed fetchDisasterMessages scheduled task");
        return messages;
    }

    private String getElementContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}