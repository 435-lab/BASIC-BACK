package com.example.basicback.report.service;

import com.example.basicback.report.dto.request.ReportRequest;
import com.example.basicback.report.dto.response.ReportResponse;
import com.example.basicback.report.entity.Report;
import com.example.basicback.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    @Autowired
    private final ReportRepository reportRepository;

    public List<ReportResponse> selectReportList() {
        return reportRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ReportResponse selectOneReport(int reportId) {
        return reportRepository.findById(reportId)
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Transactional
    public int updateDetail(ReportRequest request) {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setTitle(request.getTitle());
        report.setContent(request.getContent());
        report.setUpdateAt(LocalDateTime.now());
        reportRepository.save(report);
        return 1;
    }

    @Transactional
    public int deleteReport(int reportId) {
        reportRepository.deleteById(reportId);
        return 1;
    }

    @Transactional
    public int insertReport(ReportRequest request) {
        Report report = new Report();
        report.setTitle(request.getTitle());
        report.setContent(request.getContent());

        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserId = authentication.getName();  // 사용자 ID 가져오기

            // 사용자 정보가 UserDetails일 경우
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                currentUserId = userDetails.getUsername();  // 또는 userDetails에서 이메일을 가져오는 것도 가능
            }

            report.setCreateId(currentUserId);  // 작성자 ID 저장
            report.setCreateAt(LocalDateTime.now());
            report.setUpdateAt(LocalDateTime.now());
            reportRepository.save(report);
        } else {
            throw new RuntimeException("User is not authenticated");
        }

        return 1;
    }



    private ReportResponse convertToResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setReportId(report.getReportId());
        response.setTitle(report.getTitle());
        response.setContent(report.getContent());
        response.setCreateId(report.getCreateId());
        response.setCreateAt(report.getCreateAt().toString());
        return response;
    }

    public List<Report> getAllReports() {
        // 데이터베이스에서 모든 Report 데이터를 가져옵니다.
        return reportRepository.findAll();
    }

    public String getConcatenatedContent() {
        // 모든 Report의 content를 연결하여 하나의 문자열로 만듭니다.
        List<Report> reports = getAllReports();
        return reports.stream()
                .map(Report::getContent)
                .reduce("", (a, b) -> a + ". " + b);
    }

}
