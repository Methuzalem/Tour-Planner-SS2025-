package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.service.ImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * Endpoint for importing tours and logs from a .tpexp file
     * @param fileContent The content of the .tpexp file
     * @return ResponseEntity with status message
     */
    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestBody byte[] fileContent) {
        try {
            int importedCount = importExportService.importFile(fileContent);
            return ResponseEntity.ok("Import successful. Imported " + importedCount + " items.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Import failed: " + e.getMessage());
        }
    }
}
