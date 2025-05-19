package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.LogItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogManager {
    private ObservableList<LogItem> logList = FXCollections.observableArrayList(
            new LogItem("1","15.11.2025", 10, "Dies ist meine Notiz für eine schwere Route zum 15.11.2025!", 10.5, "2:30", 5),
            new LogItem("1","16.11.2025", 8, "Dies ist meine Notiz für eine mittlere Route zum 16.11.2025!", 10.5, "2:30", 5),
            new LogItem("1","17.11.2025", 5, "Dies ist meine Notiz für eine leichte Route zum 17.11.2025!", 10.5, "2:30", 5),
            new LogItem("2","16.11.2025", 8, "Dies ist meine Notiz für eine mittlere Route zum 16.11.2025!", 5.00, "1:30", 3),
            new LogItem("3","17.11.2025", 2, "Dies ist meine Notiz für eine leichte Route zum 17.11.2025!", 3.5, "0:30", 5)
    );

    // read the list of tours
    public ObservableList<LogItem> getTourList() {
        return logList;
    }
}
