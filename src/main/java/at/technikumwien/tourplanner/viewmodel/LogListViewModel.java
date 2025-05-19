package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LogListViewModel {
    private final LogManager logManager;
    private final LocalDate today = LocalDate.now();
    private final PropertyChangeSupport logSelectedEvent = new PropertyChangeSupport(this);
    private final PropertyChangeSupport createLogEvent = new PropertyChangeSupport(this);
    private final ObservableList<LogItem> filteredLogs = FXCollections.observableArrayList();

    public LogListViewModel(LogManager logManager) {
        this.logManager = logManager;
    }

    public void onAddLogButtonClick(PropertyChangeListener listener) {
        logSelectedEvent.addPropertyChangeListener(listener);
    }

    public void addCreateNewLogListener(PropertyChangeListener listener) {
        createLogEvent.addPropertyChangeListener(listener);
    }

    public void loadLogsForTour(String tourId) {
        filteredLogs.setAll(
                getLogList().stream()
                        .filter(log -> log.getTourId().equals(tourId)) //check if selected Tour equals TourIDs in List
                        .toList()
        );
    }

    public ObservableList<LogItem> getLogList() {
        return logManager.getTourList();
    }

    public void createNewLog() {
        LogItem newLog = new LogItem(today);
        createLogEvent.firePropertyChange(Event.EDIT_LOG, null, newLog);
    }

    public ObservableList<LogItem> getFilteredLogs() {
        return filteredLogs;
    }
}
