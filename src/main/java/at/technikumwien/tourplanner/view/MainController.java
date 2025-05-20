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

    private void updateDynamicContent() {
        dynamicContent.getChildren().clear();
        String fxmlToLoad = null;

        // Determine which FXML file to load based on the current state of the view model using switch-case
        switch (mainViewModel.getView()) {
            case "editTour":
                fxmlToLoad = "/at/technikumwien/tourplanner/edit-tour.fxml";
                break;
            case "viewTour":
                fxmlToLoad = "/at/technikumwien/tourplanner/view-tour.fxml";
                break;
            case "editLog":
                fxmlToLoad = "/at/technikumwien/tourplanner/view-tour.fxml";
                break;
        }

        if (fxmlToLoad != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));

                // Set a controller factory that provides the appropriate controller instances
                loader.setControllerFactory(controllerClass -> {
                    if (controllerClass == ViewTourController.class) {
                        return new ViewTourController(viewTourViewModel);
                    } else if( controllerClass == EditTourController.class) {
                        return new EditTourController(editTourViewModel);
                    } else if (controllerClass == LogListController.class) {
                        return new LogListController(logListViewModel);
                    }
                    // Add more controller types as needed
                    return null;
                });

                Node content = loader.load();
                dynamicContent.getChildren().add(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateDynamicContent2() {
        dynamicContent2.getChildren().clear();
        String fxmlToLoad = null;

        switch (mainViewModel.getView()) {
            case "editLog":
                fxmlToLoad = "/at/technikumwien/tourplanner/edit-log.fxml";
                break;
            case "viewTour":
                fxmlToLoad = "/at/technikumwien/tourplanner/log-list.fxml";
                break;
            case "editTour":
                fxmlToLoad = "/at/technikumwien/tourplanner/log-list.fxml";
                break;
        }

        if (fxmlToLoad != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));

                loader.setControllerFactory(param -> {
                    if (param == EditLogController.class) {
                        return new EditLogController(editLogViewModel);
                    } else if (param == LogListController.class) {
                        return new LogListController(logListViewModel);
                    }
                    return null;
                });

                Node content = loader.load();
                dynamicContent2.getChildren().add(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
