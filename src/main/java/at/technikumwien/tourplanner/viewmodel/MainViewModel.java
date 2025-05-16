package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.TourManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MainViewModel {
    private final TourManager tourManager;
    private final TourViewModel tourViewModel;
    private TourItem selectedTourItem = null;

    public MainViewModel(TourManager tourManager, TourViewModel tourViewModel) {
        this.tourManager = tourManager;
        this.tourViewModel = tourViewModel;
    }

    public void setSelectedTourItem(TourItem selectedTourItem) {
        this.selectedTourItem = selectedTourItem;
    }

    public ObservableList<TourItem> getTourList() {
        return tourManager.getTourList();
    }
}
