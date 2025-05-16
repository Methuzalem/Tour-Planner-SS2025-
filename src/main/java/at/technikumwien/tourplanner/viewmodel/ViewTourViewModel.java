package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

public class ViewTourViewModel {
    // Rename from currentTourProperty to currentTour (standard JavaFX naming convention)
    private final ObjectProperty<TourItem> currentTour = new SimpleObjectProperty<>(null);

    public ViewTourViewModel() {

    }

    // Proper JavaFX property accessor method
    public ObjectProperty<TourItem> currentTourProperty() {
        return currentTour;
    }

    public TourItem getCurrentTour() {
        return currentTour.get();
    }
}