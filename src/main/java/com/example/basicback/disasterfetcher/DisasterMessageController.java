package com.example.basicback.disasterfetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DisasterMessageController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/disaster-message")
    public ResponseEntity<List<DisasterMessage>> getDisasterMessages(@RequestParam String region) {
        String sql = "SELECT * FROM disaster_message WHERE rcptn_rgn_nm LIKE ?";
        List<DisasterMessage> messages = jdbcTemplate.query(sql, new Object[]{"%" + region + "%"},
                (rs, rowNum) -> {
                    DisasterMessage message = new DisasterMessage();
                    message.setSn(rs.getString("sn"));
                    message.setCrtDt(rs.getTimestamp("crt_dt").toLocalDateTime());
                    message.setMsgCn(rs.getString("msg_cn"));
                    message.setRcptnRgnNm(rs.getString("rcptn_rgn_nm"));
                    message.setEmrgStepNm(rs.getString("emrg_step_nm"));
                    message.setDstSeNm(rs.getString("dst_se_nm"));
                    message.setRegYmd(rs.getTimestamp("reg_ymd").toLocalDateTime());
                    message.setMdfcnYmd(rs.getTimestamp("mdfcn_ymd").toLocalDateTime());
                    return message;
                }
        );

        return ResponseEntity.ok(messages);
    }
}