package at.technikumwien.tourplannerbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDate;

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

    @Column(columnDefinition = "TEXT")
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
}
