package at.technikumwien.tourplanner.model;

import java.util.UUID;

public record LogItem(
        String logId,
        String tourId,
        String date,
        Integer difficulty,
        String comment,
        Double totalDistance,
        String totalTime,
        Integer rating) {


    public LogItem(String date){this(UUID.randomUUID().toString(), date, 0, "", 0.00, "", 0);}

    public LogItem(
            String tourId,
            String date,
            Integer difficulty,
            String comment,
            Double totalDistance,
            String totalTime,
            Integer rating) {
        this(UUID.randomUUID().toString(), tourId, date, difficulty, comment, totalDistance, totalTime, rating);
    }

    public String getDate() {
        return date;
    }

    public Integer getDifficulty() {
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

    public Double getTotalDistance() {
        return totalDistance;
    }
}
