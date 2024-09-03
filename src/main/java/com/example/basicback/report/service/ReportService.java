package com.example.basicback.report.service;

import com.example.basicback.report.dto.request.ReportRequest;
import com.example.basicback.report.dto.response.ReportResponse;
import com.example.basicback.report.entity.Report;
import com.example.basicback.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

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
        report.setCreateId(request.getCreateId());
        report.setCreateAt(LocalDateTime.now());
        report.setUpdateAt(LocalDateTime.now());
        reportRepository.save(report);
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
}