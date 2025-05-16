package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.TourItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourManager {
    private ObservableList<TourItem> tourList = FXCollections.observableArrayList(
            new TourItem("Tour 1", true),
            new TourItem("Tour 2", false)
    );

    // read the list of tours
    public ObservableList<TourItem> getTourList() {
        return tourList;
    }

    // create a new tour
    public TourItem createNewTour(String name, boolean done) {
        TourItem newTour = new TourItem(name, done);
        tourList.add(newTour);
        return newTour;
    }
}
