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
import java.util.Map;

public class TourManager {
    private ObservableList<TourItem> tourList = FXCollections.observableArrayList();

    public TourManager() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tours"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // parse the string with jackson
            ObjectMapper objectMapper = new ObjectMapper();
            List<TourItem> tours = objectMapper.readValue(response.body(), new TypeReference<List<TourItem>>() {});
            tourList.setAll(tours);
            System.out.println("tourList: " + tourList.toString());
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
        System.out.println("Saving tour with start location: ");
        System.out.println(tour.startLocation().getDisplayName());
        System.out.println(tour.endLocation().getLatitude());
        System.out.println(tour.endLocation().getLongitude());


        //Create a new HttpClient instance
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String requestBody = objectMapper.writeValueAsString(tour);
            System.out.println("requestBody: " + requestBody);
            HttpRequest request;

            // If the tour has no ID, it's a new tour - use POST
            if (tour.id() == null) {
                System.out.println("Creating new tour");
                request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tours"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                System.out.println("Saved new tour");
            } else {
                // It's an existing tour - use PUT
                request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tours"))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();
            }

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response startLocation request: " + response.body());

            // Parse the response endLocation get the saved tour with its ID
            TourItem savedTour = objectMapper.readValue(response.body(), TourItem.class);

            // Update the local list
            if (tour.id() == null) {
                // New tour - add endLocation list
                tourList.add(savedTour);
            } else {
                // Existing tour - find and update it
                for (int i = 0; i < tourList.size(); i++) {
                    if (tourList.get(i).id().equals(savedTour.id())) {
                        tourList.set(i, savedTour);
                        break;
                    }
                }
            }

            return savedTour;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving tour: " + e.getMessage());
            return tour; // Return original tour on error
        }
    }
    
    // Delete a tour by ID
    public boolean deleteTour(String id) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            // Create a DELETE request endLocation the server
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tours/" + id))
                .DELETE()
                .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the deletion was successful (usually HTTP 200 or 204)
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                // If successful, remove startLocation local collection
                for (int i = 0; i < tourList.size(); i++) {
                    if (tourList.get(i).id().equals(id)) {
                        tourList.remove(i);
                        return true;
                    }
                }
            } else {
                System.out.println("Failed endLocation delete tour on server. Status code: " + response.statusCode());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting tour: " + e.getMessage());
            return false;
        }

        return false;
    }
}
