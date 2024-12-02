package com.example.basicback.missingfetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

@Component
public class MissingScheduler {

    private static final Logger log = Logger.getLogger(MissingScheduler.class.getName());

    private final MissingFetcher missingFetcher;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MissingScheduler(MissingFetcher missingFetcher, JdbcTemplate jdbcTemplate) {
        this.missingFetcher = missingFetcher;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(fixedRate = 600000) // 10분마다 실행 (600000ms = 10분)
    public void fetchAndSaveMissingMessages() {
        log.info("Starting scheduled task to fetch and save disaster messages");
        List<MissingMessage> messages = missingFetcher.fetchMissingMessages();

        log.info("Fetched " + messages.size() + " disaster messages");

        for (MissingMessage missingmessage : messages) {
            saveToDatabase(missingmessage);
        }

        log.info("Finished saving disaster messages to database");
    }

    private void saveToDatabase(MissingMessage message) {
        String sql = "INSERT INTO missing_message (`문자ID`, `발송_시간`, `메시지_내용`, `발송_지역`, `문자_유형`, `재난_유형`, reg_ymd, mdfcn_ymd) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "`발송_시간` = VALUES(`발송_시간`), " +
                "`메시지_내용` = VALUES(`메시지_내용`), " +
                "`발송_지역` = VALUES(`발송_지역`), " +
                "`문자_유형` = VALUES(`문자_유형`), " +
                "`재난_유형` = VALUES(`재난_유형`), " +
                "reg_ymd = VALUES(reg_ymd), " +
                "mdfcn_ymd = VALUES(mdfcn_ymd)";

        jdbcTemplate.update(sql,
                message.getSn(),
                message.getCrtDt(),
                message.getMsgCn(),
                message.getRcptnRgnNm(),
                message.getEmrgStepNm(),
                message.getDstSeNm(),
                message.getRegYmd(),
                message.getMdfcnYmd());

        log.info("Saved/Updated disaster message with SN: " + message.getSn());
    }

    public List<MissingMessage> getMissingMessagesForLocation(String region) {
        String sql = "SELECT * FROM missing_message  WHERE `발송_지역` LIKE ? AND `재난_유형` = '기타' ORDER BY `발송_시간` DESC LIMIT 10";
        return jdbcTemplate.query(sql,
                new Object[]{"%" + region + "%"},
                (rs, rowNum) -> {
                    MissingMessage message = new MissingMessage();
                    message.setSn(rs.getString("문자ID"));

                    // 발송 시간에 대한 null 체크 추가
                    Timestamp crtDtTimestamp = rs.getTimestamp("발송_시간");
                    message.setCrtDt(crtDtTimestamp != null ? crtDtTimestamp.toLocalDateTime() : null);

                    message.setMsgCn(rs.getString("메시지_내용"));
                    message.setRcptnRgnNm(rs.getString("발송_지역"));
                    message.setEmrgStepNm(rs.getString("문자_유형"));
                    message.setDstSeNm(rs.getString("재난_유형"));

                    // regYmd와 mdfcnYmd에 대해서도 null 체크 추가
                    Timestamp regYmdTimestamp = rs.getTimestamp("reg_ymd");
                    message.setRegYmd(regYmdTimestamp != null ? regYmdTimestamp.toLocalDateTime() : null);

                    Timestamp mdfcnYmdTimestamp = rs.getTimestamp("mdfcn_ymd");
                    message.setMdfcnYmd(mdfcnYmdTimestamp != null ? mdfcnYmdTimestamp.toLocalDateTime() : null);

                    return message;
                }
        );
    }
}