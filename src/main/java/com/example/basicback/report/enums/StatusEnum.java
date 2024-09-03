package com.example.basicback.report.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    SUBMITTED(1, "제출됨"),
    IN_PROGRESS(2, "처리 중"),
    COMPLETED(3, "완료됨"),
    REJECTED(4, "거부됨");

    int statusCode;
    String description;

    StatusEnum(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }
}