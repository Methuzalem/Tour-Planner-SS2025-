package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.TourItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class TourManager {
    private ObservableList<TourItem> tourList = FXCollections.observableArrayList(
            new TourItem("Tour 1"),
            new TourItem("Tour 2")
    );

    // read the list of tours
    public ObservableList<TourItem> getTourList() {
        return tourList;
    }

    // create or update a tour
    public TourItem saveTour(TourItem tour) {
        // If the tour has no ID, it's a new tour
        if (tour.id() == null) {
            String newId = UUID.randomUUID().toString();
            TourItem newTour = new TourItem(
                newId,
                tour.name(),
                tour.description(),
                tour.from(),
                tour.to(),
                tour.transportType(),
                tour.distance(),
                tour.estimatedTime(),
                tour.routeInformation()
            );
            tourList.add(newTour);
            return newTour;
        } else {
            // It's an existing tour, find and update it
            for (int i = 0; i < tourList.size(); i++) {
                if (tourList.get(i).id().equals(tour.id())) {
                    tourList.set(i, tour);
                    return tour;
                }
            }
            // If we get here, we didn't find a matching tour
            // This shouldn't happen in normal operation, but we'll add the tour anyway
            tourList.add(tour);
            return tour;
        }
    }
    
    // Convenience method for adding a brand new tour
    public TourItem createNewTour(TourItem tourWithoutId) {
        return saveTour(tourWithoutId);
    }
    
    // Find a tour by ID
    public TourItem findTourById(String id) {
        for (TourItem tour : tourList) {
            if (tour.id().equals(id)) {
                return tour;
            }
        }
        return null;
    }
}
