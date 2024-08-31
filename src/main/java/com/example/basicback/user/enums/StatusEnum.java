package com.example.basicback.user.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    OK(200, "OK"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    CONFLICT(409, "CONFLICT"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

    int statusCode;
    String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }


}