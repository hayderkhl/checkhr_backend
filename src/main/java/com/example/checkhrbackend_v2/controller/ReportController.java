package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.Leaves;
import com.example.checkhrbackend_v2.service.LeaveService;
import com.example.checkhrbackend_v2.service.PdfService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private PdfService pdfService;

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);


    @GetMapping("/leaves/user/{userId}/year/{year}")
    public ResponseEntity<InputStreamResource> generateLeaveReport(@PathVariable Long userId, @PathVariable int year) throws DocumentException, IOException {
        List<Leaves> leaves = leaveService.getLeavesByUserIdAndYear(userId, year);
        String userName = leaveService.getUserFullNameById(userId);
        logger.info("Your message here " + userName);

        ByteArrayInputStream pdf = pdfService.generatePdfReport(userId, year, leaves, userName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=leaves_report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
