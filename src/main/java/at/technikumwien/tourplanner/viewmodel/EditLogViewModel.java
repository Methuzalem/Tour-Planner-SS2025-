package at.technikumwien.tourplanner.viewmodel;


import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import javafx.beans.property.*;

import java.time.LocalDate;

public class EditLogViewModel {
    private final LogManager logManager;
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty comment = new SimpleStringProperty();
    private final DoubleProperty difficulty = new SimpleDoubleProperty();
    private final StringProperty totalTime = new SimpleStringProperty();
    private final StringProperty totalDistance = new SimpleStringProperty();
    private final IntegerProperty rating = new SimpleIntegerProperty();

    public EditLogViewModel(LogManager logManager) {this.logManager = logManager;}

    public void loadLog(LogItem logItem) {
        date.set(logItem.getDate());
        comment.set(logItem.getComment());
        difficulty.set(logItem.getDifficulty());
        totalTime.set(logItem.getTotalTime());
        totalDistance.set(logItem.getTotalDistance());
        rating.set(logItem.getRating());
    }

    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty commentProperty() { return comment; }
    public DoubleProperty difficultyProperty() { return difficulty; }
    public StringProperty totalTimeProperty() { return totalTime; }
    public StringProperty totalDistanceProperty() { return totalDistance; }
    public IntegerProperty ratingProperty() { return rating; }
}
