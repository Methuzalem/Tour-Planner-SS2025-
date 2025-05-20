package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LogListViewModel {
    private final LogManager logManager;
    private final LocalDate today = LocalDate.now();
    private final ObjectProperty<LogItem> selectedLog = new SimpleObjectProperty<>();
    private final PropertyChangeSupport createLogEvent = new PropertyChangeSupport(this);
    private final ObservableList<LogItem> filteredLogs = FXCollections.observableArrayList();
    //    private final ObjectProperty<TourItem> currentTour = new SimpleObjectProperty<>(null);

    public LogListViewModel(LogManager logManager) {
        this.logManager = logManager;
    }

    public void addCreateNewLogListener(PropertyChangeListener listener) {
        createLogEvent.addPropertyChangeListener(listener);
    }

    public void loadLogsForTour(String tourId) {
        filteredLogs.setAll(
                getLogList().stream()
                        .filter(log -> tourId != null && tourId.equals(log.getTourId())) //check if selected Tour equals TourIDs in List
                        .toList()
        );
    }

    public ObservableList<LogItem> getLogList() {
        return logManager.getLogList();
    }

    public void createNewLog() {
        LogItem newLog = new LogItem(today);
        createLogEvent.firePropertyChange(Event.CREATE_LOG, null, newLog);
    }

    public ObservableList<LogItem> getFilteredLogs() {
        return filteredLogs;
    }

    public void editLog() {
        createLogEvent.firePropertyChange(Event.EDIT_LOG, null, selectedLog.get());
    }

    public ObjectProperty<LogItem> selectedLogProperty() {
        return selectedLog;
    }
}
