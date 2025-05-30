package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.utils.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.UUID;

public class LogManager {
    private final PropertyChangeSupport createNewLogEvent = new PropertyChangeSupport(this);

    LocalDate datum1 = LocalDate.of(2025, 11, 15);
    LocalDate datum2 = LocalDate.of(2025, 11, 16);
    LocalDate datum3 = LocalDate.of(2025, 11, 17);

    private ObservableList<LogItem> logList = FXCollections.observableArrayList(
            new LogItem("1",datum1, 10.00, "Dies ist meine Notiz für eine schwere Route zum 15.11.2025!", "10.5", "2:30", "5"),
            new LogItem("1",datum2, 8.00, "Dies ist meine Notiz für eine mittlere Route zum 16.11.2025!", "10.5", "2:30", "5"),
            new LogItem("1",datum3, 5.00, "Dies ist meine Notiz für eine leichte Route zum 17.11.2025!", "10.5", "2:30", "5"),
            new LogItem("2",datum2, 8.00, "Dies ist meine Notiz für eine mittlere Route zum 16.11.2025!", "5.00", "1:30", "3"),
            new LogItem("3",datum3, 2.00, "Dies ist meine Notiz für eine leichte Route zum 17.11.2025!", "3.5", "0:30", "5")
    );

    // read the list of tours
    public ObservableList<LogItem> getLogList() {
        return logList;
    }

    public void addCreateLogListener(PropertyChangeListener listener) {
        createNewLogEvent.addPropertyChangeListener(listener);
    }

    public void saveLog(LogItem logItem) {
        if (logItem.logId() == null) {
            String newId = UUID.randomUUID().toString();
            LogItem newItem = new LogItem(
                    newId,
                    logItem.tourId(),
                    logItem.date(),
                    logItem.difficulty(),
                    logItem.comment(),
                    logItem.totalTime(),
                    logItem.totalDistance(),
                    logItem.rating()
            );
            logList.add(newItem);
        } else {
            //update if existing
            for (int i = 0; i < logList.size(); i++) {
                if (logList.get(i).logId().equals(logItem.logId())) {
                    logList.set(i, logItem);
                    createNewLogEvent.firePropertyChange(Event.REFRESH_LOG, null, logItem);
                    return;
                }
            }
        }

        createNewLogEvent.firePropertyChange(Event.REFRESH_LOG, null, logItem);
    }

    public void deleteLog(LogItem logItem) {
        if (logItem == null) {
            return;
        }
        for (int i = 0; i < logList.size(); i++) {
            if (logList.get(i).logId().equals(logItem.logId())) {
                logList.remove(i);
                createNewLogEvent.firePropertyChange(Event.REFRESH_LOG, null, logItem);
            }
        }
    }
}
