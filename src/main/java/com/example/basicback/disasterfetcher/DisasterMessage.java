package com.example.basicback.disasterfetcher;


import java.time.LocalDateTime;

public class DisasterMessage {
    private String locationName;
    private String message;
    private String md101Sn;
    private LocalDateTime createDate;

    // Getters and Setters
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMd101Sn() {
        return md101Sn;
    }

    public void setMd101Sn(String md101Sn) {
        this.md101Sn = md101Sn;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
