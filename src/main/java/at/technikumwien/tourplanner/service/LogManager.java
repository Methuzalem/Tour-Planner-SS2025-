package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.model.TourItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogManager {
    private ObservableList<LogItem> logList = FXCollections.observableArrayList(
            new LogItem("15.11.2025", "Schwer", "Dies ist meine Notiz für eine schwere Route zum 15.11.2025!"),
            new LogItem("16.11.2025", "Mittel", "Dies ist meine Notiz für eine mittlere Route zum 16.11.2025!"),
            new LogItem("17.11.2025", "Leicht", "Dies ist meine Notiz für eine leichte Route zum 17.11.2025!")
    );

    // read the list of tours
    public ObservableList<LogItem> getTourList() {
        return logList;
    }
}
