package com.example.basicback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class DisasterMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonProperty("location_id")
    private String locationId;

    @Column(name = "location_name", columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String locationName;

    @JsonProperty("md101_sn")
    private String md101Sn;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("send_platform")
    private String sendPlatform;
}