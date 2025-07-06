package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.service.ImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportExportController {

    private final ImportExportService importExportService;

    @Autowired
    public ImportExportController(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    /**
     * Endpoint for exporting all tours and logs as a .tpexp file (CSV format)
     * @return ResponseEntity containing the file data as a downloadable attachment
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        byte[] exportData = importExportService.generateExportFile();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "tourplanner-export.tpexp");
        headers.setContentLength(exportData.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(exportData);
    }
}
