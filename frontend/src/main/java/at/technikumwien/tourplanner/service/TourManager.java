package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.TourItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

public class TourManager {
    private ObservableList<TourItem> tourList = FXCollections.observableArrayList();

    public TourManager() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // parse the string with jackson
            ObjectMapper objectMapper = new ObjectMapper();
            List<TourItem> tours = objectMapper.readValue(response.body(), new TypeReference<List<TourItem>>() {});
            tourList.setAll(tours);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching tours: " + e.getMessage());
        }

    }

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
