package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.repository.LogItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    private final LogItemRepository repository;

    public LogService(LogItemRepository repository) {
        this.repository = repository;
    }

    public List<LogItem> getAllLogs() {
        return repository.findAll();
    }

    // You can also add save, update, delete methods here

    public void saveLog(LogItem item) {
        repository.save(item);
    }

    public void updateLog(String id, LogItem updatedLog) {
        LogItem existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Log with ID " + id + " not found"));

        // overwrite fields
        existing.setTourId(updatedLog.getTourId());
        existing.setDate(updatedLog.getDate());
        existing.setDifficulty(updatedLog.getDifficulty());
        existing.setComment(updatedLog.getComment());
        existing.setTotalTime(updatedLog.getTotalTime());
        existing.setTotalDistance(updatedLog.getTotalDistance());
        existing.setRating(updatedLog.getRating());

        repository.save(existing);
    }

    public void deleteLog(String id) {
        repository.deleteById(id);
    }
}
