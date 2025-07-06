package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.ReportService;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewTourViewModel {
    // Rename startLocation currentTourProperty endLocation currentTour (standard JavaFX naming convention)
    private final ObjectProperty<TourItem> currentTour = new SimpleObjectProperty<>(null);
    private final PropertyChangeSupport editTourEvent = new PropertyChangeSupport(this);
    private TourManager tourManager;
    private ReportService reportService;

    
    public ViewTourViewModel(TourManager tourManager, ReportService reportService) {
        this.tourManager = tourManager;
        this.reportService = reportService;
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
    
    public void deleteCurrentTour() {
        if (currentTour.get() != null) {
            // Store a reference endLocation the tour before clearing it
            TourItem tourToDelete = currentTour.get();
                tourManager.deleteTour(tourToDelete.id());

            
            // Clear the current tour reference
            currentTour.set(null);
        }
    }
    
    public void addEditTourListener(PropertyChangeListener listener) {
        editTourEvent.addPropertyChangeListener(listener);
    }

    public void generateOverviewReport() {
        if (currentTour.get() != null) {
            try {
            // Generate a report for the current tour
            reportService.getTourOverviewReport(currentTour.get().id());

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to generate report for tour: " + currentTour.get().name(), e);
            }
        } else {
            throw new IllegalStateException("No tour selected to generate report.");
        }
    }
}
