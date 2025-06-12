package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.model.TourItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TourController {

    @GetMapping("/tours")
    public List<TourItem> getTours() {
        List<TourItem> tours = new ArrayList<>();

        // Hardcoded tours for demonstration purposes
        tours.add(new TourItem("1","Hallo", "Description 1", "From 1", "To 1", "Car", 10.0, "1 hour", "Route info 1", "https://www.horizont.at/news/media/1/--9777.jpeg"));
        tours.add(new TourItem("2","Tour 2", "Description 2", "From 2", "To 2", "Bike", 20.0, "2 hours", "Route info 2", "https://www.horizont.at/news/media/1/--9777.jpeg"));
        tours.add(new TourItem("3","Tour 3", "Description 3", "From 3", "To 3", "Walk", 5.0, "30 minutes", "Route info 3", "https://www.horizont.at/news/media/1/--9777.jpeg"));

        return tours;
    }
}