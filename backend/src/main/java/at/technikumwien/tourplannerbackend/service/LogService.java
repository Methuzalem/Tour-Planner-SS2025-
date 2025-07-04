package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.repository.LogItemRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class LogService {
    private final LogItemRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    public LogService(LogItemRepository repository) {
        this.repository = repository;
    }

    public List<LogItem> getAllLogs() {
        logger.debug("Loading all logs from database");
        return repository.findAll();
    }

    public void saveLog(LogItem item) {
        logger.debug("Saving new log: {}", item);
        repository.save(item);
    }

    public void updateLog(String id, LogItem updatedLog) {
        logger.debug("Updating log with ID: {}", id);

        LogItem existing = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Log with ID {} not found – update failed", id);
                    return new IllegalArgumentException("Log with ID " + id + " not found");
                });

        logger.debug("Old log state: {}", existing);
        logger.debug("Updated log data: {}", updatedLog);

        // overwrite fields
        existing.setTourId(updatedLog.getTourId());
        existing.setDate(updatedLog.getDate());
        existing.setDifficulty(updatedLog.getDifficulty());
        existing.setComment(updatedLog.getComment());
        existing.setTotalTime(updatedLog.getTotalTime());
        existing.setTotalDistance(updatedLog.getTotalDistance());
        existing.setRating(updatedLog.getRating());

        repository.save(existing);
        logger.info("Log with ID {} successfully updated", id);
    }

    public void deleteLog(String id) {
        logger.info("Deleting log with ID: {}", id);
        repository.deleteById(id);
    }
}
