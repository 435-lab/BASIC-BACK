package com.example.basicback.disasterfetcher;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disaster_message")
public class DisasterMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sn")
    private String sn;

    @Column(name = "crt_dt")
    private LocalDateTime crtDt;

    @Column(name = "msg_cn", length = 4000)
    private String msgCn;

    @Column(name = "rcptn_rgn_nm", length = 4000)
    private String rcptnRgnNm;

    @Column(name = "emrg_step_nm", length = 100)
    private String emrgStepNm;

    @Column(name = "dst_se_nm", length = 100)
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