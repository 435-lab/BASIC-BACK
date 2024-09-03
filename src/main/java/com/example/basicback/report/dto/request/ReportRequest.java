package com.example.basicback.report.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportRequest {
    private int reportId;
    private String title;
    private String content;
    private String createId;
    private String createAt;
    private String updateAt;
}