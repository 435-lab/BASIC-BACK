package com.example.basicback.disasterfetcher;


import java.util.List;

public class DisasterApiResponse {
    private List<Row> row;

    public List<Row> getRow() {
        return row;
    }

    public void setRow(List<Row> row) {
        this.row = row;
    }

    public static class Row {
        private String locationName;
        private String msg;
        private String md101Sn;
        private String createDate;

        // Getters and Setters
        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMd101Sn() {
            return md101Sn;
        }

        public void setMd101Sn(String md101Sn) {
            this.md101Sn = md101Sn;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
