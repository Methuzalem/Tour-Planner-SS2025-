package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.TourItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourService {
    private final TourItemRepository repository;

    public TourService(TourItemRepository repository) {
        this.repository = repository;
    }

    public List<TourItem> getAllTours() {
        return repository.findAll();
    }

    public TourItem saveTour(TourItem tour) {
        return repository.save(tour);
    }

    public void deleteTour(String id) {
        repository.deleteById(id);
    }

    public TourItem getTourById(String id) {
        return repository.findById(id).orElse(null);
    }
}
