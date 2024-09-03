package com.example.basicback.report.controller;

import com.example.basicback.report.dto.request.ReportRequest;
import com.example.basicback.report.dto.response.ReportResponse;
import com.example.basicback.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String report(Model model) {
        List<ReportResponse> reportResponse = reportService.selectReportList();
        System.out.println("report reportResponse ==> " + reportResponse);
        model.addAttribute("reports", reportResponse);
        return "report/report";
    }

    @RequestMapping(value = "/report-detail/{reportId}", method = RequestMethod.GET)
    public String reportDetail(@PathVariable("reportId") int reportId, Model model) {
        ReportResponse reportResponse = reportService.selectOneReport(reportId);
        System.out.println("reportDetail reportResponse ==> " + reportResponse);
        model.addAttribute("report", reportResponse);
        return "report/reportDetail";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public int updateDetail(@RequestBody ReportRequest reportRequest) {
        System.out.println("updateDetail reportRequest ==> " + reportRequest);
        return reportService.updateDetail(reportRequest);
    }

    @DeleteMapping("/delete/{reportId}")
    @ResponseBody
    public int deleteReport(@PathVariable("reportId") int reportId) {
        System.out.println("deleteReport reportId ==> " + reportId);
        return reportService.deleteReport(reportId);
    }

    @RequestMapping(value = "/insert-view", method = RequestMethod.GET)
    public String reportInsertView() {
        return "report/reportInsert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public int insertReport(@RequestBody ReportRequest reportRequest) {
        System.out.println("insertReport reportRequest ==> " + reportRequest);
        return reportService.insertReport(reportRequest);
    }
}