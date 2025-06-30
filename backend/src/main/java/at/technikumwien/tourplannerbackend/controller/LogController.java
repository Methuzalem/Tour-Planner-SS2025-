package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {
    private final LogService logService;

    // Let Spring inject the service here
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    public List<LogItem> getLogs() {
        return logService.getAllLogs();
    }
}
