package at.technikumwien.tourplanner.viewmodel;


import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.utils.Event;
import at.technikumwien.tourplanner.utils.RatingOption;
import javafx.beans.property.*;
import javafx.scene.control.Alert;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

public class EditLogViewModel {
    private final LogManager logManager;
    private final PropertyChangeSupport cancelLogEditEvent = new PropertyChangeSupport(this);

    //Properties for all Log fields
    private final SimpleStringProperty logId = new SimpleStringProperty(null);
    private final SimpleStringProperty tourId = new SimpleStringProperty(null);
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty comment = new SimpleStringProperty();
    private final DoubleProperty difficulty = new SimpleDoubleProperty();
    private final StringProperty totalTime = new SimpleStringProperty();
    private final ObjectProperty<RatingOption> rating = new SimpleObjectProperty<>();

    public EditLogViewModel(LogManager logManager) {this.logManager = logManager;}

    // Property getters
    public SimpleStringProperty logIdProperty() { return logId; }
    public SimpleStringProperty tourIdProperty() { return tourId; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty commentProperty() { return comment; }
    public DoubleProperty difficultyProperty() { return difficulty; }
    public StringProperty totalTimeProperty() { return totalTime; }
    public ObjectProperty<RatingOption> ratingProperty() { return rating; }


    public void loadLog(LogItem logItem) {
        logId.set(logItem.getLogId());
        tourId.set(logItem.getTourId());
        date.set(logItem.getDate());
        comment.set(logItem.getComment());
        difficulty.set(logItem.getDifficulty());
        totalTime.set(logItem.getTotalTime());
        int ratingValue = Integer.parseInt(logItem.getRating());
        rating.set(RatingOption.fromValue(ratingValue));
    }

    private void resetFormFields() {
        logId.set(null);
        tourId.set(null);
        date.set(null);
        comment.set("");
        difficulty.set(0.00);
        totalTime.set("");
        rating.set(null);
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

    public void createNewLog() {
        LocalDate logDate = date.get();
        double diff = difficulty.get();
        String time = totalTime.get();

        if (logDate == null) {
            showAlert("Invalid Input", "Please enter a date.");
            return;
        }

        if (time == null || time.isBlank()) {
            showAlert("Invalid Input", "Please enter your total time!");
            return;
        }

        if (comment.get() == null){
            comment.set("No comment for this Log!");
        }

        if (rating.get() == null) {
            showAlert("Invalid Input", "Please select a rating!");
            return;
        }

        LogItem logToSave = new LogItem(
                logId.get(),
                tourId.get(),
                logDate,
                diff,
                comment.get(),
                time,
                String.valueOf(rating.get().getValue())
        );
        logManager.saveLog(logToSave);
        cancelLogEditEvent.firePropertyChange(Event.CANCEL_LOG, null, logToSave);
        resetFormFields();
    }


    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void prepareNewLogForTour(String id) {
        this.tourId.set(id);
    }
}
