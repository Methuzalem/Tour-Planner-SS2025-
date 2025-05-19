package at.technikumwien.tourplanner.model;

import java.util.UUID;

public record LogItem(
        String logId,
        String tourId,
        String date,
        String difficulty,
        String note) {


    public LogItem(String date){this(UUID.randomUUID().toString(), "", date, "", "");}

    public LogItem(
            String tourId,
            String date,
            String difficulty,
            String note) {
        this(UUID.randomUUID().toString(), tourId, date, difficulty, note);
    }

    public String getDate() {
        return date;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getNote() {
        return note;
    }

    public Object getTourId() {
        return tourId;
    }
}
