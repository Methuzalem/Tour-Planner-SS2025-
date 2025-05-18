package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.SimpleStringProperty;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EditTourViewModel {
    private final TourManager tourManager;
    private final PropertyChangeSupport cancelEditEvent = new PropertyChangeSupport(this);

    private final SimpleStringProperty newTourName = new SimpleStringProperty();

    public SimpleStringProperty newTourNameProperty() {
        return newTourName;
    }

    public EditTourViewModel(TourManager tourManager) {
        this.tourManager = tourManager;
    }

    public void createNewTour() {
        TourItem tourItem = new TourItem(newTourName.get());
        this.tourManager.createNewTour(tourItem);

        // reset the entry form
        newTourNameProperty().set("");
    }

    public void cancelEdit() {
        // Reset the new tour name
        newTourNameProperty().set("");
        // Notify listeners that editing has been canceled
        cancelEditEvent.firePropertyChange(Event.CANCEL_EDIT, null, null);
    }

    public void addCancelEditListener(PropertyChangeListener listener) {
        cancelEditEvent.addPropertyChangeListener(listener);
    }
}
