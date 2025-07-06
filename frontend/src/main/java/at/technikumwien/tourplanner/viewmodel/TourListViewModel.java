package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.ImportExportService;
import at.technikumwien.tourplanner.service.ReportService;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class TourListViewModel {
    private final TourManager tourManager;
    private final ImportExportService importExportService;
    private final ReportService reportService;
    private final PropertyChangeSupport tourSelectedEvent = new PropertyChangeSupport(this);
    private final PropertyChangeSupport createTourEvent = new PropertyChangeSupport(this);

    public TourListViewModel(TourManager tourManager, ImportExportService importExportService, ReportService reportService) {
        this.tourManager = tourManager;
        this.importExportService = importExportService;
        this.reportService = reportService;
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

    public void exportTours() {
        boolean success = importExportService.exportData();
        if (!success) {
            // You might want to show an error message to the user here
            System.out.println("Export failed. Check logs for more details.");
        }
    }

    public void importTours(File file) {
        boolean success = importExportService.importData(file);
        if (!success) {
            // You might want to show an error message to the user here
            System.out.println("Import failed. Check logs for more details.");
        } else {
            // Refresh the tour list after successful import
            tourManager.refreshTourList();
        }
    }

    public void generateSummaryReport() {
            try {
                // Generate a summary report for the current tour
                reportService.getTourSummaryReport();

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to generate summary report", e);
            }
    }
}
