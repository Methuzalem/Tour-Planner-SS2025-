package at.technikumwien.tourplannerbackend.repository;

import at.technikumwien.tourplannerbackend.model.LogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogItemRepository extends JpaRepository<LogItem, String> {
    @Query("SELECT l FROM LogItem l WHERE l.tourId = :tourId")
    List<LogItem> findByTourId(@Param("tourId") String tourId);
}