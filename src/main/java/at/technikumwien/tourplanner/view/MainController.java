package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.viewmodel.EditTourViewModel;
import at.technikumwien.tourplanner.viewmodel.LogListViewModel;
import at.technikumwien.tourplanner.viewmodel.MainViewModel;
import at.technikumwien.tourplanner.viewmodel.ViewTourViewModel;
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
    private final LogListViewModel logListViewModel;

    public MainController(MainViewModel mainViewModel, ViewTourViewModel viewTourViewModel, EditTourViewModel editTourViewModel, LogListViewModel logListViewModel) {
        this.mainViewModel = mainViewModel;
        this.viewTourViewModel = viewTourViewModel;
        this.editTourViewModel = editTourViewModel;
        this.logListViewModel = logListViewModel;
    }

    @FXML
    public void initialize() {
        // Listen for changes in selectedTourItem and isEditing to update the view
        mainViewModel.viewProperty().addListener((observable, oldValue, newValue) -> {
            updateDynamicContent();
            updateDynamicContent2();
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikumwien/tourplanner/log-list.fxml"));

            // Controller mit ViewModel setzen
            loader.setControllerFactory(param -> {
                if (param == LogListController.class) {
                    return new LogListController(logListViewModel);  // <-- hier musst du logListViewModel vorher definieren
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
