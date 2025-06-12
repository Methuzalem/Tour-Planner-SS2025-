package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TourListViewModel {
    private final TourManager tourManager;
    private final PropertyChangeSupport tourSelectedEvent = new PropertyChangeSupport(this);
    private final PropertyChangeSupport createTourEvent = new PropertyChangeSupport(this);

    public TourListViewModel(TourManager tourManager) {
        this.tourManager = tourManager;
    }

    public void addTourSelectedListener(PropertyChangeListener listener) {
        tourSelectedEvent.addPropertyChangeListener(listener);
    }

    public void addCreateNewTourListener(PropertyChangeListener listener) {
        createTourEvent.addPropertyChangeListener(listener);
    }

    public ObservableList<TourItem> getTourList() {
        return tourManager.getTourList();
    }

    public void selectTour(TourItem tourItem) {
        // Null is allowed as the old value here since we're focusing on the new selection
        tourSelectedEvent.firePropertyChange(Event.TOUR_SELECTED, null, tourItem);
    }

    public void createNewTour() {
        TourItem newTour = new TourItem("New Tour");
        createTourEvent.firePropertyChange(Event.EDIT_TOUR, null, newTour);
    }
}
