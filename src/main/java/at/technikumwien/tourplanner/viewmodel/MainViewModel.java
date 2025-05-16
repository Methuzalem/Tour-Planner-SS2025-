package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;

public class MainViewModel {
    private final TourManager tourManager;
    private final TourListViewModel tourListViewModel;
    private final ViewTourViewModel viewTourViewModel;
    private final SimpleStringProperty view = new SimpleStringProperty("viewTour");

    public MainViewModel(TourManager tourManager, TourListViewModel tourListViewModel, ViewTourViewModel viewTourViewModel) {
        this.tourManager = tourManager;
        this.tourListViewModel = tourListViewModel;
        this.viewTourViewModel = viewTourViewModel;

        tourListViewModel.addTourSelectedListener(evt -> {
            if (evt.getPropertyName().equals(Event.TOUR_SELECTED)) {
                TourItem newTourItem = (TourItem) evt.getNewValue();
                viewTourViewModel.currentTourProperty().set(newTourItem);
                setView("viewTour");

            }
        });

        tourListViewModel.editTourListListener(evt -> {
            if (evt.getPropertyName().equals(Event.EDIT_TOUR)) {
                TourItem newTourItem = (TourItem) evt.getNewValue();
                setView("editTour");
            }
        });
    }

    // get view
    public String getView() {
        return view.get();
    }

    // get view property
    public StringProperty viewProperty() {
        return view;
    }

    private void setView(String view) {
        this.view.set(view);
    }

}
