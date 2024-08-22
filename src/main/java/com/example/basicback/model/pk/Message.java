package com.example.basicback.model.pk;

import com.example.basicback.enums.StatusEnum;
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
        this.result = null;
        this.message = null;
        this.data = null;
    }
}