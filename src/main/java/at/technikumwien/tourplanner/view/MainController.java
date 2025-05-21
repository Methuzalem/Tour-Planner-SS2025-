package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.utils.Event;
import at.technikumwien.tourplanner.viewmodel.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private StackPane dynamicContent;

    @FXML
    private StackPane dynamicContent2;

    private final MainViewModel mainViewModel;
    private final ViewTourViewModel viewTourViewModel;
    private final EditTourViewModel editTourViewModel;
    private final TourListViewModel tourListViewModel;
    private final LogListViewModel logListViewModel;
    private final EditLogViewModel editLogViewModel;
    private final LogManager logManager;

    private final Map<String, Node> contentCache = new HashMap<>();
    private final Map<String, Node> content2Cache = new HashMap<>();

    public MainController(MainViewModel mainViewModel, ViewTourViewModel viewTourViewModel, EditTourViewModel editTourViewModel, TourListViewModel tourListViewModel, LogListViewModel logListViewModel, EditLogViewModel editLogViewModel, LogManager logManager) {
        this.mainViewModel = mainViewModel;
        this.viewTourViewModel = viewTourViewModel;
        this.editTourViewModel = editTourViewModel;
        this.tourListViewModel = tourListViewModel;
        this.logListViewModel = logListViewModel;
        this.editLogViewModel = editLogViewModel;
        this.logManager = logManager;
    }

    @FXML
    public void initialize() {
        // Listen for changes in selectedTourItem and isEditing to update the view
        mainViewModel.viewProperty().addListener((observable, oldValue, newValue) -> {
            updateDynamicContent();
            updateDynamicContent2();
        });

        // if Tour is picked, give accurate Log-List
        tourListViewModel.addTourSelectedListener(evt -> {
            if (evt.getNewValue() instanceof TourItem tour) {
                logListViewModel.loadLogsForTour(tour.id());
            }
        });

        // if log is created refresh list
        logManager.addCreateLogListener(evt -> {
            if (evt.getPropertyName().equals(Event.REFRESH_LOG)) {
                LogItem logItem = (LogItem) evt.getNewValue();
                logListViewModel.loadLogsForTour(logItem.tourId());
            }
        });
        
        // Initial update
        updateDynamicContent();
        updateDynamicContent2();
    }

    private Node loadAndCacheContent(String fxmlPath, Map<String, Node> cache) {
        if (cache.containsKey(fxmlPath)) {
            return cache.get(fxmlPath);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == ViewTourController.class) {
                    return new ViewTourController(viewTourViewModel);
                } else if (controllerClass == EditTourController.class) {
                    return new EditTourController(editTourViewModel);
                } else if (controllerClass == LogListController.class) {
                    return new LogListController(logListViewModel);
                } else if (controllerClass == EditLogController.class) {
                    return new EditLogController(editLogViewModel);
                }
                return null;
            });

            Node content = loader.load();
            cache.put(fxmlPath, content);
            return content;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateDynamicContent() {
        dynamicContent.getChildren().clear();
        String fxmlToLoad = switch (mainViewModel.getView()) {
            case "editTour" -> "/at/technikumwien/tourplanner/edit-tour.fxml";
            case "viewTour", "editLog" -> "/at/technikumwien/tourplanner/view-tour.fxml";
            default -> null;
        };

        if (fxmlToLoad != null) {
            Node content = loadAndCacheContent(fxmlToLoad, contentCache);
            if (content != null) {
                dynamicContent.getChildren().add(content);
            }
        }
    }

    private void updateDynamicContent2() {
        dynamicContent2.getChildren().clear();
        String fxmlToLoad = switch (mainViewModel.getView()) {
            case "editLog" -> "/at/technikumwien/tourplanner/edit-log.fxml";
            case "viewTour", "editTour" -> "/at/technikumwien/tourplanner/log-list.fxml";
            default -> null;
        };

        if (fxmlToLoad != null) {
            Node content = loadAndCacheContent(fxmlToLoad, content2Cache);
            if (content != null) {
                dynamicContent2.getChildren().add(content);
            }
        }
    }
}
