package com.example.basicback.missingfetcher;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "missing_message")
public class MissingMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "문자ID")
    private String sn;

    @Column(name = "발송_시간")
    private LocalDateTime crtDt;

    @Column(name = "메시지_내용", length = 4000)
    private String msgCn;

    @Column(name = "발송_지역", length = 4000)
    private String rcptnRgnNm;

    @Column(name = "문자_유형", length = 100)
    private String emrgStepNm;

    @Column(name = "재난_유형", length = 100)
    private String dstSeNm;

    @Column(name = "reg_ymd")
    private LocalDateTime regYmd;

    @Column(name = "mdfcn_ymd")
    private LocalDateTime mdfcnYmd;


    // Getters and Setters
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public LocalDateTime getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(LocalDateTime crtDt) {
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

    public LocalDateTime getRegYmd() {
        return regYmd;
    }

    public void setRegYmd(LocalDateTime regYmd) {
        this.regYmd = regYmd;
    }

    public LocalDateTime getMdfcnYmd() {
        return mdfcnYmd;
    }

    public void setMdfcnYmd(LocalDateTime mdfcnYmd) {
        this.mdfcnYmd = mdfcnYmd;
    }
}