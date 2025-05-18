package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewTourViewModel {
    // Rename from currentTourProperty to currentTour (standard JavaFX naming convention)
    private final ObjectProperty<TourItem> currentTour = new SimpleObjectProperty<>(null);
    private final PropertyChangeSupport editTourEvent = new PropertyChangeSupport(this);

    public ViewTourViewModel() {

    }

    // Proper JavaFX property accessor method
    public ObjectProperty<TourItem> currentTourProperty() {
        return currentTour;
    }

    public TourItem getCurrentTour() {
        return currentTour.get();
    }
    
    public void setCurrentTour(TourItem tour) {
        currentTour.set(tour);
    }
    
    public void editCurrentTour() {
        if (currentTour.get() != null) {
            // Notify listeners that the current tour should be edited
            editTourEvent.firePropertyChange(Event.EDIT_TOUR, null, currentTour.get());
        }
    }
    
    public void addEditTourListener(PropertyChangeListener listener) {
        editTourEvent.addPropertyChangeListener(listener);
    }
}
