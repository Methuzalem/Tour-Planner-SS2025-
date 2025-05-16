package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.TourItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourManager {
    private ObservableList<TourItem> tourList = FXCollections.observableArrayList(
            new TourItem("Tour 1"),
            new TourItem("Tour 2")
    );

    // read the list of tours
    public ObservableList<TourItem> getTourList() {
        return tourList;
    }

    // create a new tour
    public TourItem createNewTour(TourItem newTour) {
        tourList.add(newTour);
        return newTour;
    }
}
