package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.service.LogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    // GET /logs
    @GetMapping
    public List<LogItem> getLogs() {
        return logService.getAllLogs();
    }

    // POST /logs
    @PostMapping
    public void createLog(@RequestBody LogItem logItem) {
        logService.saveLog(logItem);
    }

    // PUT /logs/{id}
    @PutMapping("/{id}")
    public void updateLog(@PathVariable String id, @RequestBody LogItem updatedLog) {
        logService.updateLog(id, updatedLog);
    }

    //DELETE /logs/{id}
    @DeleteMapping("/{id}")
    public void deleteLog(@PathVariable String id) {
        logService.deleteLog(id);
    }
}
