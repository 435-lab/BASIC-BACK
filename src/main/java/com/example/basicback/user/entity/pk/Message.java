package com.example.basicback.user.entity.pk;

import com.example.basicback.user.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class Message {

    private LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    private int status;
    private Boolean result;
    private String message;
    private Object data;

    public Message() {
        this.status = StatusEnum.BAD_REQUEST.getStatusCode();
        this.result = false;  // null 대신 false로 초기화
        this.message = "";    // null 대신 빈 문자열로 초기화
        this.data = null;
    }

    // result의 getter 메서드 이름을 isResult()로 명시적으로 지정
    public boolean isResult() {
        return result != null && result;
    }

    // result의 setter 메서드
    public void setResult(Boolean result) {
        this.result = result;
    }
}