package com.example.basicback.report.controller;

import com.example.basicback.report.dto.request.ReportRequest;
import com.example.basicback.report.dto.response.ReportResponse;
import com.example.basicback.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports() {
        List<ReportResponse> reportResponse = reportService.selectReportList();
        return ResponseEntity.ok(reportResponse);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable("reportId") int reportId) {
        ReportResponse reportResponse = reportService.selectOneReport(reportId);
        return ResponseEntity.ok(reportResponse);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<Void> updateReport(@PathVariable("reportId") int reportId, @RequestBody ReportRequest reportRequest) {
        reportRequest.setReportId(reportId); // Ensure reportId in request matches URL
        reportService.updateDetail(reportRequest);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable("reportId") int reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping
    public ResponseEntity<Void> createReport(@RequestBody ReportRequest reportRequest) {
        reportService.insertReport(reportRequest);
        return ResponseEntity.status(201).build(); // 201 Created
    }
}
