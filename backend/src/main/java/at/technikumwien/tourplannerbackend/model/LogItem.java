package at.technikumwien.tourplannerbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "log_items")
public class LogItem {

    @Id
    @Column(name = "log_id", nullable = false, updatable = false)
    private String logId;

    @Column(name = "tour_id", nullable = false)
    private String tourId;

    @Column(nullable = false)
    private LocalDate date;

    private Double difficulty;

    private String comment;

    @Column(name = "total_time")
    private String totalTime;

    @Column(name = "total_distance")
    private String totalDistance;

    private String rating;

    public LogItem() {}

    public LogItem(String logId, String tourId, LocalDate date, Double difficulty, String comment,
                   String totalTime, String totalDistance, String rating) {
        this.logId = logId;
        this.tourId = tourId;
        this.date = date;
        this.difficulty = difficulty;
        this.comment = comment;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.rating = rating;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * Returns a CSV formatted string for export purposes.
     * Format: L,logId,tourId,date,difficulty,comment,totalTime,totalDistance,rating
     */
    @JsonIgnore
    public String getExportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("L,");
        sb.append(escapeField(logId)).append(",");
        sb.append(escapeField(tourId)).append(",");
        sb.append(date != null ? date : "").append(",");
        sb.append(difficulty != null ? difficulty : "").append(",");
        sb.append(escapeField(comment)).append(",");
        sb.append(escapeField(totalTime)).append(",");
        sb.append(escapeField(totalDistance)).append(",");
        sb.append(escapeField(rating));

        return sb.toString();
    }

    // Helper method to escape commas in fields
    private String escapeField(String field) {
        if (field == null) {
            return "";
        }
        // If the field contains commas or quotes, wrap it in quotes and escape any quotes
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * Creates a LogItem from an export string
     * @param exportString The export string in CSV format
     * @return A new LogItem instance
     */
    public static LogItem fromExportString(String exportString) {
        List<String> fields = parseCsvLine(exportString);

        if (fields.size() < 9 || !fields.get(0).equals("L")) {
            throw new IllegalArgumentException("Invalid log export string format");
        }

        int index = 1; // Skip the type field

        String logId = fields.get(index++);
        String tourId = fields.get(index++);

        // Parse date
        LocalDate date = null;
        String dateStr = fields.get(index++);
        if (dateStr != null && !dateStr.isEmpty()) {
            date = LocalDate.parse(dateStr);
        }

        Double difficulty = fields.get(index).isEmpty() ? null : Double.parseDouble(fields.get(index++));
        String comment = fields.get(index++);
        String totalTime = fields.get(index++);
        String totalDistance = fields.get(index++);
        String rating = fields.get(index);

        return new LogItem(
            logId, tourId, date, difficulty, comment,
            totalTime, totalDistance, rating
        );
    }

    /**
     * Parses a CSV line into fields, handling quoted values
     */
    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Check if this is an escaped quote
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++; // Skip the next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }

        // Add the last field
        result.add(field.toString());
        return result;
    }
}
