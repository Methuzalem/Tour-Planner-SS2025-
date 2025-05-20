package at.technikumwien.tourplanner.viewmodel;


import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

public class EditLogViewModel {
    private final LogManager logManager;
    private final PropertyChangeSupport cancelLogEditEvent = new PropertyChangeSupport(this);
    private final PropertyChangeSupport createNewLogEvent = new PropertyChangeSupport(this);
    private final PropertyChangeSupport validationLogErrorEvent = new PropertyChangeSupport(this);

    //Properties for all Log fields
    private final SimpleStringProperty logId = new SimpleStringProperty(null);
    private final SimpleStringProperty tourId = new SimpleStringProperty(null);
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty comment = new SimpleStringProperty();
    private final DoubleProperty difficulty = new SimpleDoubleProperty();
    private final StringProperty totalTime = new SimpleStringProperty();
    private final StringProperty totalDistance = new SimpleStringProperty();
    private final StringProperty rating = new SimpleStringProperty();

    public EditLogViewModel(LogManager logManager) {this.logManager = logManager;}

    // Property getters
    public SimpleStringProperty logIdProperty() { return logId; }
    public SimpleStringProperty tourIdProperty() { return tourId; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty commentProperty() { return comment; }
    public DoubleProperty difficultyProperty() { return difficulty; }
    public StringProperty totalTimeProperty() { return totalTime; }
    public StringProperty totalDistanceProperty() { return totalDistance; }
    public StringProperty ratingProperty() { return rating; }


    public void loadLog(LogItem logItem) {
        date.set(logItem.getDate());
        comment.set(logItem.getComment());
        difficulty.set(logItem.getDifficulty());
        totalTime.set(logItem.getTotalTime());
        totalDistance.set(logItem.getTotalDistance());
        rating.set(logItem.getRating());
    }

    private void resetFormFields() {
        logId.set(null);
        tourId.set(null);
        date.set(null);
        comment.set("");
        difficulty.set(0.00);
        totalTime.set("");
        totalDistance.set("");
        rating.set("0");
    }

    public void cancelEditLog() {
        // Reset all form fields
        resetFormFields();
        // Notify listeners that editing has been canceled
        cancelLogEditEvent.firePropertyChange(Event.CANCEL_LOG, null, null);
    }

    public void addCancelLogEditListener(PropertyChangeListener listener) {
        cancelLogEditEvent.addPropertyChangeListener(listener);
    }

    public void addCreateLogListener(PropertyChangeListener listener) {
        createNewLogEvent.addPropertyChangeListener(listener);
    }

    public void createNewLog() {

        LogItem logToSave = new LogItem(
                logId.get(),
                tourId.get(),
                date.get(),
                difficulty.get(),
                comment.get(),
                totalTime.get(),
                totalDistance.get(),
                rating.get()
        );

        logManager.saveLog(logToSave); // oder .add() bei dir
        cancelLogEditEvent.firePropertyChange(Event.CANCEL_LOG, null, null);
        resetFormFields();
    }


    public void prepareNewLogForTour(String id) {
        this.tourId.set(id);
    }
}
