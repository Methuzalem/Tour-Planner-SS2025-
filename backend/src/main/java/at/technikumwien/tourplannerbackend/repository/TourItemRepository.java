package at.technikumwien.tourplannerbackend.repository;

import at.technikumwien.tourplannerbackend.model.TourItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourItemRepository extends JpaRepository<TourItem, String> {
}
