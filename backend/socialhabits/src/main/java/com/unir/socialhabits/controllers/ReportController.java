package com.unir.socialhabits.controllers;

import com.unir.socialhabits.services.ReportService;


import com.unir.socialhabits.dto.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "PDF report generation")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Generate user PDF report")
    @GetMapping(value="/user/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport(@PathVariable UUID id) {

        byte[] pdf = reportService.generateUserReport(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}