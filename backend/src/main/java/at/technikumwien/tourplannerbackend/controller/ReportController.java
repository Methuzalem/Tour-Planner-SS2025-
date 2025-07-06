package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.service.ReportService;
import at.technikumwien.tourplannerbackend.service.TourService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public class ReportController {
    private final TourService tourService;
    private final ReportService reportService;

    public ReportController(TourService tourService, ReportService reportService) {
        this.tourService = tourService;
        this.reportService = reportService;
    }

    @GetMapping("/report/{tourId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String tourId) {
        byte[] pdfBytes = reportService.generateTourOverviewReport(tourId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "tour-" + tourId + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/summary-report")
    public ResponseEntity<byte[]> generateSummary() {
        byte[] pdfBytes = reportService.generateTourSummaryReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // Format current date for the filename
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        headers.setContentDispositionFormData("attachment", "tour-summary-" + currentDate + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
