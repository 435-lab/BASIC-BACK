package com.example.basicback.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "DisasterMsg")
public class DisasterApiResponse {

    @JacksonXmlProperty(localName = "row")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Row> row;

    @Data
    public static class Row {
        @JacksonXmlProperty(localName = "create_date")
        private String createDate;

        @JacksonXmlProperty(localName = "location_name")
        private String locationName;

        @JacksonXmlProperty(localName = "md101_sn")
        private String md101Sn;

        @JacksonXmlProperty(localName = "msg")
        private String msg;
    }
}