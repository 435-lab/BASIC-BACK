package com.example.basicback.disasterfetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DisasterMessageController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/disaster-message")
    public ResponseEntity<List<DisasterMessage>> getDisasterMessages(@RequestParam String region) {
        String sql = "SELECT * FROM disaster_message WHERE location_name LIKE ?";
        List<DisasterMessage> messages = jdbcTemplate.query(sql, new Object[]{"%" + region + "%"},
                (rs, rowNum) -> {
                    DisasterMessage message = new DisasterMessage();
                    message.setLocationName(rs.getString("location_name"));
                    message.setMessage(rs.getString("message"));
                    message.setMd101Sn(rs.getString("md101_sn"));
                    message.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                    return message;
                }
        );

        return ResponseEntity.ok(messages);
    }
}