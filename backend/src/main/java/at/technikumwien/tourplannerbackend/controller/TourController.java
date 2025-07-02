package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.TourItemRepository;
import at.technikumwien.tourplannerbackend.service.TourService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TourController {
    private final TourService tourService;

    // Let Spring inject the service here
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public List<TourItem> getTours() {
        return tourService.getAllTours();
    }

    @PostMapping("/tours")
    public TourItem saveTour(@RequestBody TourItem tour) {
        return tourService.saveTour(tour);
    }

    @PutMapping("/tours")
    public TourItem updateTour(@RequestBody TourItem tour) {
        return tourService.saveTour(tour);
    }

    @DeleteMapping("/tours/{id}")
    public void deleteTour(@PathVariable String id) {
        tourService.deleteTour(id);
    }
}
