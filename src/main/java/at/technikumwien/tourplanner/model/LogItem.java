package at.technikumwien.tourplanner.model;

import java.time.LocalDate;
import java.util.UUID;

public record LogItem(
        String logId,
        String tourId,
        LocalDate date,
        Double difficulty,
        String comment,
        String totalDistance,
        String totalTime,
        Integer rating) {


    public LogItem(LocalDate date){this(UUID.randomUUID().toString(), date, 0.00, "", "0.00", "", 0);}

    public LogItem(
            String tourId,
            LocalDate date,
            Double difficulty,
            String comment,
            String totalDistance,
            String totalTime,
            Integer rating) {
        this(UUID.randomUUID().toString(), tourId, date, difficulty, comment, totalDistance, totalTime, rating);
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

    public Object getTourId() {
        return tourId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getTotalDistance() {
        return totalDistance;
    }
}
