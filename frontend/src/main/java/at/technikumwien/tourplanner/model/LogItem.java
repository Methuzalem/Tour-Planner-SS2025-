package at.technikumwien.tourplanner.model;

import java.time.LocalDate;
import java.util.UUID;

public record LogItem(
        String logId,
        String tourId,
        LocalDate date,
        Double difficulty,
        String comment,
        String totalTime,
        String rating) {

    public LogItem(LocalDate date){this(null, "", date, 0.00, "", "", "0");}

    public LogItem(
            String tourId,
            LocalDate date,
            Double difficulty,
            String comment,
            String totalTime,
            String rating) {
        this(UUID.randomUUID().toString(), tourId, date, difficulty, comment, totalTime, rating);
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public String getComment() {
        return comment;
    }

    public String getTourId() {
        return tourId;
    }

    public String getRating() {
        return rating;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getLogId() { return logId; }
}
