package at.technikumwien.tourplanner.model;

import java.util.UUID;

public record LogItem(
        String logId,
        String date,
        String difficulty,
        String note) {


    public LogItem(String date){this(UUID.randomUUID().toString(), date, "", "");}

    public LogItem(
            String date,
            String difficulty,
            String note) {
        this(UUID.randomUUID().toString(), date, difficulty, note);
    }


}
