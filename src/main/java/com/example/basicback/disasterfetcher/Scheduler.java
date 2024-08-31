package com.example.basicback.disasterfetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class Scheduler {

    private static final Logger log = Logger.getLogger(Scheduler.class.getName());

    private final DisasterFetcher disasterFetcher;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Scheduler(DisasterFetcher disasterFetcher, JdbcTemplate jdbcTemplate) {
        this.disasterFetcher = disasterFetcher;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(fixedRate = 600000) // 10분마다 실행 (600000ms = 10분)
    public void fetchAndSaveDisasterMessages() {
        log.info("Starting scheduled task to fetch and save disaster messages");
        List<DisasterMessage> messages = disasterFetcher.fetchDisasterMessages();

        log.info("Fetched " + messages.size() + " disaster messages");

        for (DisasterMessage message : messages) {
            saveToDatabase(message);
        }

        log.info("Finished saving disaster messages to database");
    }

    private void saveToDatabase(DisasterMessage message) {
        String sql = "INSERT INTO disaster_messages (location_name, message, md101_sn, create_date) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "location_name = VALUES(location_name), " +
                "message = VALUES(message), " +
                "create_date = VALUES(create_date)";

        jdbcTemplate.update(sql,
                message.getLocationName(),
                message.getMessage(),
                message.getMd101Sn(),
                message.getCreateDate());

        log.info("Saved/Updated disaster message with MD101 SN: " + message.getMd101Sn());
    }

    public List<DisasterMessage> getDisasterMessagesForLocation(double lat, double lng) {
        return jdbcTemplate.query(
                "SELECT * FROM disaster_messages",
                (rs, rowNum) -> {
                    DisasterMessage message = new DisasterMessage();
                    message.setLocationName(rs.getString("location_name"));
                    message.setMessage(rs.getString("message"));
                    message.setMd101Sn(rs.getString("md101_sn"));
                    message.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                    return message;
                }
        );
    }
}