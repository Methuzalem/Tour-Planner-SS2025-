package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.service.LogService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {
    private final LogService logService;
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    public LogController(LogService logService) {
        this.logService = logService;
    }

    // GET /logs
    @GetMapping
    public List<LogItem> getLogs() {
        logger.info("GET /logs called");
        return logService.getAllLogs();
    }

    // POST /logs
    @PostMapping
    public void createLog(@RequestBody LogItem logItem) {
        logger.info("POST /logs - Log created: {}", logItem);
        logService.saveLog(logItem);
    }

    // PUT /logs/{id}
    @PutMapping("/{id}")
    public void updateLog(@PathVariable String id, @RequestBody LogItem updatedLog) {
        logger.info("PUT /logs/{} - Updating log with data: {}", id, updatedLog);
        logService.updateLog(id, updatedLog);
    }

    //DELETE /logs/{id}
    @DeleteMapping("/{id}")
    public void deleteLog(@PathVariable String id) {
        logger.info("DELETE /logs/{} - Log deleted", id);
        logService.deleteLog(id);
    }
}
