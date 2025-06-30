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
}
