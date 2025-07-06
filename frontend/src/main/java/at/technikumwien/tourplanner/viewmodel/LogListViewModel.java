package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.utils.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class LogListViewModel {
    private final LogManager logManager;
    private final LocalDate today = LocalDate.now();
    private final ObjectProperty<LogItem> selectedLog = new SimpleObjectProperty<>();
    private final PropertyChangeSupport createLogEvent = new PropertyChangeSupport(this);
    private final ObservableList<LogItem> filteredLogs = FXCollections.observableArrayList();
    private final ObservableList<LogItem> filteredLogsOriginal = FXCollections.observableArrayList();


    public LogListViewModel(LogManager logManager) {
        this.logManager = logManager;
    }

    public void addCreateNewLogListener(PropertyChangeListener listener) {
        createLogEvent.addPropertyChangeListener(listener);
    }

    public void loadLogsForTour(String tourId) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/logs"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            List<LogItem> logs = objectMapper.readValue(response.body(), new TypeReference<List<LogItem>>() {});

            //Two lists with filtered data to restore original list (filtered of tours) after filter
            Platform.runLater(() -> {
                filteredLogs.setAll(
                        logs.stream()
                                .filter(log -> tourId == null || tourId.equals(log.getTourId()))
                                .toList()
                );
            });

            Platform.runLater(() -> {
                filteredLogsOriginal.setAll(logs.stream()
                        .filter(log -> tourId == null || tourId.equals(log.getTourId()))
                        .toList());
                filteredLogs.setAll(filteredLogsOriginal);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ObservableList<LogItem> getLogList() {
        return logManager.getLogList();
    }

    public void createNewLog() {
        LogItem newLog = new LogItem(today);
        createLogEvent.firePropertyChange(Event.CREATE_LOG, null, newLog);
    }

    public ObservableList<LogItem> getFilteredLogs() {
        return filteredLogs;
    }

    public void editLog() {
        createLogEvent.firePropertyChange(Event.EDIT_LOG, null, selectedLog.get());
    }

    public ObjectProperty<LogItem> selectedLogProperty() {
        return selectedLog;
    }

    public void deleteLog() {
        logManager.deleteLog(selectedLog.get());
    }


    public void filterLogs(String query) {
        String lowerQuery = query.toLowerCase();

        List<LogItem> filtered = filteredLogsOriginal.stream()
                .filter(log ->
                        log.getComment().toLowerCase().contains(lowerQuery) ||
                                log.getRating().toLowerCase().contains(lowerQuery) ||
                                String.valueOf(log.getTotalTime()).toLowerCase().contains(lowerQuery) ||
                                log.getDate().toString().contains(lowerQuery)
                )
                .toList();

        Platform.runLater(() -> filteredLogs.setAll(filtered));
    }
}
