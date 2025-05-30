package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.TourItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class TourManager {
    private ObservableList<TourItem> tourList = FXCollections.observableArrayList(
        new TourItem("1","Tour 1", "Description 1", "From 1", "To 1", "Car", 10.0, "1 hour", "Route info 1", "https://www.horizont.at/news/media/1/--9777.jpeg"),
        new TourItem("2","Tour 2", "Description 2", "From 2", "To 2", "Bike", 20.0, "2 hours", "Route info 2", "https://www.horizont.at/news/media/1/--9777.jpeg"),
        new TourItem("3","Tour 3", "Description 3", "From 3", "To 3", "Walk", 5.0, "30 minutes", "Route info 3", "https://www.horizont.at/news/media/1/--9777.jpeg")
    );

    // read the list of tours
    public ObservableList<TourItem> getTourList() {
        return tourList;
    }

    // create or update a tour
    public TourItem saveTour(TourItem tour) {
        System.out.println("Saving tour: " + tour.id());
        // If the tour has no ID, it's a new tour
        if (tour.id() == null) {
            String newId = UUID.randomUUID().toString();
            TourItem newTour = new TourItem(
                newId,
                tour.name(),
                tour.description(),
                tour.from(),
                tour.to(),
                tour.distance(),
                tour.transportType(),
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
            System.out.println("Tour with ID " + tour.id() + " not found, adding as new tour.");
            tourList.add(tour);
            return tour;
        }
    }
    
    // Delete a tour by ID
    public boolean deleteTour(String id) {
        for (int i = 0; i < tourList.size(); i++) {
            if (tourList.get(i).id().equals(id)) {
                tourList.remove(i);
                return true;
            }
        }
        return false;
    }
}
