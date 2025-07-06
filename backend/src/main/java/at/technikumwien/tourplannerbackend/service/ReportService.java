package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.LogItemRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final TourService tourService;
    private final LogItemRepository logItemRepository;

    @Autowired
    public ReportService(TourService tourService, JdbcTemplate jdbcTemplate, LogItemRepository logItemRepository) {
        this.tourService = tourService;
        this.logItemRepository = logItemRepository;
    }

    public byte[] generateTourOverviewReport(String tourId) {
        // Get tour data
        TourItem tour = tourService.getTourById(tourId);
        if (tour == null) {
            throw new RuntimeException("Tour not found with ID: " + tourId);
        }

        // Get associated logs
        List<LogItem> logs = logItemRepository.findByTourId(tourId);

        // Generate the PDF
        return createTourOverviewPDF(tour, logs);
    }

    private byte[] createTourOverviewPDF(TourItem tour, List<LogItem> logs) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Tour Overview Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add tour name as subtitle
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph subtitle = new Paragraph(tour.getName(), subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(15);
            document.add(subtitle);

            // Add tour details section
            addTourDetailsSection(document, tour);

            // Add logs section
            addLogsSection(document, logs);

            // Add footer with generation timestamp
            Paragraph footer = new Paragraph("Report generated on: " + LocalDate.now().toString(),
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            footer.setSpacingBefore(20);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }

        return baos.toByteArray();
    }

    private void addTourDetailsSection(Document document, TourItem tour) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph tourInfoTitle = new Paragraph("Tour Details", sectionFont);
        tourInfoTitle.setSpacingBefore(15);
        tourInfoTitle.setSpacingAfter(10);
        document.add(tourInfoTitle);

        // Add tour details in a table format
        PdfPTable tourTable = new PdfPTable(2);
        tourTable.setWidthPercentage(100);
        tourTable.setSpacingBefore(10f);
        tourTable.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {0.3f, 0.7f};
        tourTable.setWidths(columnWidths);

        addTableRow(tourTable, "Name", tour.getName());
        addTableRow(tourTable, "Description", tour.getDescription());

        // Format start location
        String startLocation = tour.getStartLocation().getDisplayName();
        if (tour.getStartLocation().getLatitude() != 0 || tour.getStartLocation().getLongitude() != 0) {
            startLocation += " (" + tour.getStartLocation().getLatitude() + ", " +
                    tour.getStartLocation().getLongitude() + ")";
        }
        addTableRow(tourTable, "Start Location", startLocation);

        // Format end location
        String endLocation = tour.getEndLocation().getDisplayName();
        if (tour.getEndLocation().getLatitude() != 0 || tour.getEndLocation().getLongitude() != 0) {
            endLocation += " (" + tour.getEndLocation().getLatitude() + ", " +
                    tour.getEndLocation().getLongitude() + ")";
        }
        addTableRow(tourTable, "End Location", endLocation);

        addTableRow(tourTable, "Transport Type", tour.getTransportType());
        addTableRow(tourTable, "Distance", formatDistance(tour.getDistance()));
        addTableRow(tourTable, "Estimated Time", formatTime(tour.getEstimatedTime()));
        addTableRow(tourTable, "Route Information", tour.getRouteInformation());

        document.add(tourTable);
    }

    private void addLogsSection(Document document, List<LogItem> logs) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph logsTitle = new Paragraph("Tour Logs", sectionFont);
        logsTitle.setSpacingBefore(20);
        logsTitle.setSpacingAfter(10);
        document.add(logsTitle);

        if (logs.isEmpty()) {
            Paragraph noLogs = new Paragraph("No logs available for this tour.");
            document.add(noLogs);
            return;
        }

        // Add log statistics
        Paragraph logStats = new Paragraph("Total logs: " + logs.size());
        logStats.setSpacingAfter(10);
        document.add(logStats);

        // Create logs table
        PdfPTable logsTable = new PdfPTable(5);
        logsTable.setWidthPercentage(100);

        // Add table headers
        String[] headers = {"Date", "Difficulty", "Time", "Rating", "Comment"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(220, 220, 220));
            cell.setPadding(5);
            logsTable.addCell(cell);
        }

        // Add log data rows
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LogItem log : logs) {
            logsTable.addCell(log.getDate() != null ? log.getDate().format(dateFormatter) : "N/A");
            logsTable.addCell(log.getDifficulty() != null ? String.format("%.1f", log.getDifficulty()) : "N/A");
            logsTable.addCell(formatMinutes(log.getTotalTime()));
            logsTable.addCell(log.getRating() != null ? log.getRating().toString() + "/5" : "N/A");

            PdfPCell commentCell = new PdfPCell(new Phrase(log.getComment() != null ? log.getComment() : ""));
            commentCell.setColspan(1);
            logsTable.addCell(commentCell);
        }

        document.add(logsTable);

        // Add log summary
        if (logs.size() > 1) {
            addLogSummary(document, logs);
        }
    }

    private void addLogSummary(Document document, List<LogItem> logs) throws DocumentException {
        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Paragraph summaryTitle = new Paragraph("Log Summary", summaryFont);
        summaryTitle.setSpacingBefore(15);
        summaryTitle.setSpacingAfter(5);
        document.add(summaryTitle);

        // Calculate averages
        double avgDifficulty = logs.stream()
                .filter(log -> log.getDifficulty() != null)
                .mapToDouble(LogItem::getDifficulty)
                .average()
                .orElse(0);

        double avgRating = logs.stream()
                .filter(log -> log.getRating() != null)
                .mapToInt(LogItem::getRating)
                .average()
                .orElse(0);

        double avgTime = logs.stream()
                .filter(log -> log.getTotalTime() != null)
                .mapToInt(LogItem::getTotalTime)
                .average()
                .orElse(0);

        // Add summary table
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(70);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        addTableRow(summaryTable, "Average Difficulty", String.format("%.2f", avgDifficulty));
        addTableRow(summaryTable, "Average Rating", String.format("%.1f/5", avgRating));
        addTableRow(summaryTable, "Average Time", formatMinutes((int)avgTime));

        document.add(summaryTable);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        labelCell.setPadding(5);
        labelCell.setBackgroundColor(new Color(240, 240, 240));

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A"));
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String formatDistance(Double distanceInKm) {
        if (distanceInKm == null) return "N/A";
        return String.format("%.2f km", distanceInKm);
    }

    private String formatTime(Double timeInHours) {
        if (timeInHours == null) return "N/A";

        int hours = (int) Math.floor(timeInHours);
        int minutes = (int) Math.round((timeInHours - hours) * 60);

        return String.format("%d h %d min", hours, minutes);
    }

    private String formatMinutes(Integer totalMinutes) {
        if (totalMinutes == null) return "N/A";

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        return String.format("%d h %d min", hours, minutes);
    }

    public byte[] generateTourSummaryReport() {
        // Get all tours
        List<TourItem> allTours = tourService.getAllTours();

        // Generate the PDF with all tours summary
        return createTourSummaryPDF(allTours);
    }

    private byte[] createTourSummaryPDF(List<TourItem> tours) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate()); // Landscape mode for wider table

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Tour Summary Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            if (tours.isEmpty()) {
                document.add(new Paragraph("No tours available."));
            } else {
                // Create summary table
                PdfPTable summaryTable = new PdfPTable(7); // 7 columns for the table
                summaryTable.setWidthPercentage(100);
                summaryTable.setSpacingBefore(10f);

                // Set relative column widths
                float[] columnWidths = {2f, 3f, 2f, 1.5f, 1.5f, 1.5f, 1.5f};
                summaryTable.setWidths(columnWidths);

                // Add table headers
                String[] headers = {"Name", "Description", "Transport Type", "Distance (km)", "Est. Time", "Avg. Time", "Avg. Rating"};
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(new Color(220, 220, 220));
                    cell.setPadding(5);
                    summaryTable.addCell(cell);
                }

                // Add data for each tour
                for (TourItem tour : tours) {
                    // Get logs for this tour
                    List<LogItem> tourLogs = logItemRepository.findByTourId(tour.getId());

                    // Calculate averages
                    double avgRating = tourLogs.stream()
                            .filter(log -> log.getRating() != null)
                            .mapToInt(LogItem::getRating)
                            .average()
                            .orElse(0);

                    double avgTime = tourLogs.stream()
                            .filter(log -> log.getTotalTime() != null)
                            .mapToInt(LogItem::getTotalTime)
                            .average()
                            .orElse(0);

                    // Add cells to the table
                    summaryTable.addCell(tour.getName() != null ? tour.getName() : "N/A");

                    // Description cell with limited length
                    String description = tour.getDescription();
                    if (description != null && description.length() > 50) {
                        description = description.substring(0, 47) + "...";
                    }
                    summaryTable.addCell(description != null ? description : "N/A");

                    summaryTable.addCell(tour.getTransportType() != null ? tour.getTransportType() : "N/A");
                    summaryTable.addCell(tour.getDistance() != null ? String.format("%.2f", tour.getDistance()) : "N/A");
                    summaryTable.addCell(formatTime(tour.getEstimatedTime()));
                    summaryTable.addCell(formatMinutes((int)avgTime));

                    // Format rating with 1 decimal place
                    String ratingStr = tourLogs.isEmpty() ? "N/A" : String.format("%.1f/5", avgRating);
                    summaryTable.addCell(ratingStr);
                }

                document.add(summaryTable);

                // Add summary statistics
                addOverallStatistics(document, tours);
            }

            // Add footer with generation timestamp
            Paragraph footer = new Paragraph("Report generated on: " + LocalDate.now().toString(),
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            footer.setSpacingBefore(20);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }

        return baos.toByteArray();
    }

    private void addOverallStatistics(Document document, List<TourItem> tours) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph statsTitle = new Paragraph("Overall Statistics", sectionFont);
        statsTitle.setSpacingBefore(20);
        statsTitle.setSpacingAfter(10);
        document.add(statsTitle);

        // Calculate total distance of all tours
        double totalDistance = tours.stream()
                .filter(tour -> tour.getDistance() != null)
                .mapToDouble(TourItem::getDistance)
                .sum();

        // Count transport types
        long carTours = tours.stream()
                .filter(tour -> "Car".equalsIgnoreCase(tour.getTransportType()))
                .count();

        long bikeTours = tours.stream()
                .filter(tour -> "Bike".equalsIgnoreCase(tour.getTransportType()) ||
                                "Bicycle".equalsIgnoreCase(tour.getTransportType()))
                .count();

        long walkingTours = tours.stream()
                .filter(tour -> "Walking".equalsIgnoreCase(tour.getTransportType()) ||
                                "Hike".equalsIgnoreCase(tour.getTransportType()) ||
                                "Hiking".equalsIgnoreCase(tour.getTransportType()))
                .count();

        // Add statistics table
        PdfPTable statsTable = new PdfPTable(2);
        statsTable.setWidthPercentage(60);
        statsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        addTableRow(statsTable, "Total Tours", String.valueOf(tours.size()));
        addTableRow(statsTable, "Total Distance", String.format("%.2f km", totalDistance));
        addTableRow(statsTable, "Car Tours", String.valueOf(carTours));
        addTableRow(statsTable, "Bike Tours", String.valueOf(bikeTours));
        addTableRow(statsTable, "Walking Tours", String.valueOf(walkingTours));

        document.add(statsTable);
    }
}
