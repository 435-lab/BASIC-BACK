package com.example.basicback.disasterfetcher;

import java.util.List;

public class DisasterApiResponse {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String sn;
        private String crtDt;
        private String msgCn;
        private String rcptnRgnNm;
        private String emrgStepNm;
        private String dstSeNm;
        private String regYmd;
        private String mdfcnYmd;

        // Getters and Setters
        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getCrtDt() {
            return crtDt;
        }

        public void setCrtDt(String crtDt) {
            this.crtDt = crtDt;
        }

        public String getMsgCn() {
            return msgCn;
        }

        public void setMsgCn(String msgCn) {
            this.msgCn = msgCn;
        }

        public String getRcptnRgnNm() {
            return rcptnRgnNm;
        }

        public void setRcptnRgnNm(String rcptnRgnNm) {
            this.rcptnRgnNm = rcptnRgnNm;
        }

        public String getEmrgStepNm() {
            return emrgStepNm;
        }

        public void setEmrgStepNm(String emrgStepNm) {
            this.emrgStepNm = emrgStepNm;
        }

        public String getDstSeNm() {
            return dstSeNm;
        }

        public void setDstSeNm(String dstSeNm) {
            this.dstSeNm = dstSeNm;
        }

        public String getRegYmd() {
            return regYmd;
        }

        public void setRegYmd(String regYmd) {
            this.regYmd = regYmd;
        }

        public String getMdfcnYmd() {
            return mdfcnYmd;
        }

        public void setMdfcnYmd(String mdfcnYmd) {
            this.mdfcnYmd = mdfcnYmd;
        }
    }
}