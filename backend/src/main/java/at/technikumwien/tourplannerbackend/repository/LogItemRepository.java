package at.technikumwien.tourplannerbackend.repository;

import at.technikumwien.tourplannerbackend.model.LogItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogItemRepository extends JpaRepository<LogItem, String> {
}