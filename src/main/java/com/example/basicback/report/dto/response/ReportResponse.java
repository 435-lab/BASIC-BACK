package com.example.basicback.report.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportResponse {
    private int reportId;
    private String title;
    private String content;
    private String createId;
    private String createAt;
}