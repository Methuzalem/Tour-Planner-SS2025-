package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.utils.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

public class LogManager {
    private final ObservableList<LogItem> logList = FXCollections.observableArrayList();
    private final PropertyChangeSupport createNewLogEvent = new PropertyChangeSupport(this);

    public LogManager() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/logs"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // parse the string with jackson
            ObjectMapper objectMapper = new ObjectMapper();
            List<LogItem> logs = objectMapper
                    .registerModule(new JavaTimeModule())
                    .readValue(response.body(), new TypeReference<List<LogItem>>() {});
            logList.setAll(logs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching logs: " + e.getMessage());
        }
    }

    // read the list of tours
    public ObservableList<LogItem> getLogList() {
        return logList;
    }

    public void addCreateLogListener(PropertyChangeListener listener) {
        createNewLogEvent.addPropertyChangeListener(listener);
    }

    public void saveLog(LogItem logItem) {
        if (logItem.logId() == null) {
            String newId = UUID.randomUUID().toString();
            LogItem newItem = new LogItem(
                    newId,
                    logItem.tourId(),
                    logItem.date(),
                    logItem.difficulty(),
                    logItem.comment(),
                    logItem.totalTime(),
                    logItem.totalDistance(),
                    logItem.rating()
            );
            logList.add(newItem);
        } else {
            //update if existing
            for (int i = 0; i < logList.size(); i++) {
                if (logList.get(i).logId().equals(logItem.logId())) {
                    logList.set(i, logItem);
                    createNewLogEvent.firePropertyChange(Event.REFRESH_LOG, null, logItem);
                    return;
                }
            }
        }

        createNewLogEvent.firePropertyChange(Event.REFRESH_LOG, null, logItem);
    }

    public void deleteLog(LogItem logItem) {
        if (logItem == null) {
            return;
        }
        for (int i = 0; i < logList.size(); i++) {
            if (logList.get(i).logId().equals(logItem.logId())) {
                logList.remove(i);
                createNewLogEvent.firePropertyChange(Event.REFRESH_LOG, null, logItem);
            }
        }
    }
}
