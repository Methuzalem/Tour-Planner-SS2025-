package at.technikumwien.tourplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ReportService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl = "http://localhost:8080";

    public ReportService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Downloads the tour overview report PDF for a specific tour
     *
     * @param tourId The ID of the tour to get the report for
     * @return The path to the saved PDF file
     * @throws IOException If there's an error during the HTTP request or file saving
     * @throws InterruptedException If the HTTP request is interrupted
     */
    public String getTourOverviewReport(String tourId) throws IOException, InterruptedException {
        // Build the URL for the endpoint
        String endpoint = baseUrl + "/report/" + tourId;

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        // Execute the request and get the response as byte array
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // Check if request was successful
        if (response.statusCode() == 200) {
            // Create output directory if it doesn't exist
            String outputDir = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "TourPlanner";
            Files.createDirectories(Paths.get(outputDir));

            // Save the PDF file
            String fileName = "tour-" + tourId + ".pdf";
            String filePath = outputDir + File.separator + fileName;
            Files.write(Paths.get(filePath), response.body(), StandardOpenOption.CREATE);

            return filePath;
        } else {
            throw new IOException("Failed to download report. Status code: " + response.statusCode());
        }
    }

    /**
     * Downloads the tour summary report PDF for a specific tour
     *
     * @return The path to the saved PDF file
     * @throws IOException If there's an error during the HTTP request or file saving
     * @throws InterruptedException If the HTTP request is interrupted
     */
    public String getTourSummaryReport() throws IOException, InterruptedException {
        // Build the URL for the endpoint
        String endpoint = baseUrl + "/summary-report";

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        // Execute the request and get the response as byte array
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // Check if request was successful
        if (response.statusCode() == 200) {
            // Create output directory if it doesn't exist
            String outputDir = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "TourPlanner";
            Files.createDirectories(Paths.get(outputDir));

            // Save the PDF file
            String fileName = "tour-summary-report.pdf";
            String filePath = outputDir + File.separator + fileName;
            Files.write(Paths.get(filePath), response.body(), StandardOpenOption.CREATE);

            return filePath;
        } else {
            throw new IOException("Failed to download summary report. Status code: " + response.statusCode());
        }
    }
}
