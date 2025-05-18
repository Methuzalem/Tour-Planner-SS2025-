package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EditTourViewModel {
    private final TourManager tourManager;
    private final PropertyChangeSupport cancelEditEvent = new PropertyChangeSupport(this);

    // Properties for all tour fields
    private final SimpleStringProperty id = new SimpleStringProperty(null);
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty from = new SimpleStringProperty("");
    private final SimpleStringProperty to = new SimpleStringProperty("");
    private final SimpleStringProperty transportType = new SimpleStringProperty("");
    private final SimpleDoubleProperty distance = new SimpleDoubleProperty(0.0);
    private final SimpleStringProperty estimatedTime = new SimpleStringProperty("");
    private final SimpleStringProperty routeInformation = new SimpleStringProperty("");

    public EditTourViewModel(TourManager tourManager) {
        this.tourManager = tourManager;
    }

    // Property getters
    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleStringProperty fromProperty() {
        return from;
    }

    public SimpleStringProperty toProperty() {
        return to;
    }

    public SimpleStringProperty transportTypeProperty() {
        return transportType;
    }

    public DoubleProperty distanceProperty() {
        return distance;
    }

    public SimpleStringProperty estimatedTimeProperty() {
        return estimatedTime;
    }

    public SimpleStringProperty routeInformationProperty() {
        return routeInformation;
    }

    /**
     * Load all properties from the provided TourItem
     * @param tour The tour to load data from
     */
    public void loadTour(TourItem tour) {
        if (tour == null) {
            resetFormFields();
            return;
        }
        
        id.set(tour.id());
        name.set(tour.name());
        description.set(tour.description());
        from.set(tour.from());
        to.set(tour.to());
        transportType.set(tour.transportType());
        distance.set(tour.distance());
        estimatedTime.set(tour.estimatedTime());
        routeInformation.set(tour.routeInformation());
    }

    public void createNewTour() {
        TourItem tourItem = new TourItem(
            id.get(),
            name.get(),
            description.get(),
            from.get(),
            to.get(),
            transportType.get(),
            distance.get(),
            estimatedTime.get(),
            routeInformation.get()
        );
        
        this.tourManager.saveTour(tourItem);

        // Reset all form fields
        resetFormFields();
    }

    private void resetFormFields() {
        id.set(null);
        name.set("");
        description.set("");
        from.set("");
        to.set("");
        transportType.set("");
        distance.set(0.0);
        estimatedTime.set("");
        routeInformation.set("");
    }

    public void cancelEdit() {
        // Reset all form fields
        resetFormFields();
        
        // Notify listeners that editing has been canceled
        cancelEditEvent.firePropertyChange(Event.CANCEL_EDIT, null, null);
    }

    public void addCancelEditListener(PropertyChangeListener listener) {
        cancelEditEvent.addPropertyChangeListener(listener);
    }
}
