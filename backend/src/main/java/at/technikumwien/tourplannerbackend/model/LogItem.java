package at.technikumwien.tourplannerbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;

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

    private String comment;

    @Column(name = "total_time", columnDefinition = "INTEGER")
    private Integer totalTime;

    @Column(name = "rating", columnDefinition = "INTEGER")
    private Integer rating;

    public LogItem() {}

    public LogItem(String logId, String tourId, LocalDate date, Double difficulty, String comment,
                   Integer totalTime, Integer rating) {
        this.logId = logId;
        this.tourId = tourId;
        this.date = date;
        this.difficulty = difficulty;
        this.comment = comment;
        this.totalTime = totalTime;
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

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
