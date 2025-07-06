package at.technikumwien.tourplanner.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImportExportService {
    private static final String EXPORT_URL = "http://localhost:8080/export";

    /**
     * Retrieves export data from the backend and saves it to the Downloads/Tourplanner directory
     *
     * @return true if export was successful, false otherwise
     */
    public boolean exportData() {
        try {
            // Create HTTP client and send request to backend
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(EXPORT_URL))
                    .GET()
                    .build();

            // Get response as byte array
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                System.out.println("Export failed. Server returned status code: " + response.statusCode());
                return false;
            }

            // Create file path: Downloads/Tourplanner/export_[datetime].tpexp
            String userHome = System.getProperty("user.home");
            Path downloadsPath = Paths.get(userHome, "Downloads", "Tourplanner");

            // Create directories if they don't exist
            Files.createDirectories(downloadsPath);

            // Generate filename with current timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = "tourplanner-export_" + timestamp + ".tpexp";
            File exportFile = new File(downloadsPath.toFile(), fileName);

            // Write data to file
            try (FileOutputStream fos = new FileOutputStream(exportFile)) {
                fos.write(response.body());
            }

            System.out.println("Export saved successfully to: " + exportFile.getAbsolutePath());
            return true;

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
