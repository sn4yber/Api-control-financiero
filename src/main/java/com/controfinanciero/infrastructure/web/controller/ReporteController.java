package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.service.ExcelReportService;
import com.controfinanciero.infrastructure.service.PdfReportService;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 📊 Controlador de Reportes y Exportación
 * Endpoints para generar PDFs, Excel y reportes avanzados
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final PdfReportService pdfService;
    private final ExcelReportService excelService;
    private final AuthenticationService authService;

    /**
     * 📄 GET /api/reportes/pdf
     * Genera un Estado de Cuenta en PDF
     *
     * Params:
     * - fechaInicio: fecha inicial (YYYY-MM-DD)
     * - fechaFin: fecha final (YYYY-MM-DD)
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generarPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        Usuario usuario = authService.getCurrentUser();

        byte[] pdf = pdfService.generarEstadoCuenta(usuario, fechaInicio, fechaFin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename",
                String.format("estado-cuenta-%s-%s.pdf", fechaInicio, fechaFin));

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    /**
     * 📊 GET /api/reportes/excel
     * Genera un reporte en Excel
     *
     * Params:
     * - fechaInicio: fecha inicial (YYYY-MM-DD)
     * - fechaFin: fecha final (YYYY-MM-DD)
     */
    @GetMapping("/excel")
    public ResponseEntity<byte[]> generarExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        Usuario usuario = authService.getCurrentUser();

        byte[] excel = excelService.generarReporteExcel(usuario, fechaInicio, fechaFin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("filename",
                String.format("reporte-financiero-%s-%s.xlsx", fechaInicio, fechaFin));

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }
}

