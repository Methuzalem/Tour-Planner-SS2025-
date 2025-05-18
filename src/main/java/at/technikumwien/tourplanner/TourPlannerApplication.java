package at.technikumwien.tourplanner;

import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.view.MainController;
import at.technikumwien.tourplanner.view.TourListController;
import at.technikumwien.tourplanner.view.ViewTourController;
import at.technikumwien.tourplanner.viewmodel.EditTourViewModel;
import at.technikumwien.tourplanner.viewmodel.MainViewModel;
import at.technikumwien.tourplanner.viewmodel.TourListViewModel;
import at.technikumwien.tourplanner.viewmodel.ViewTourViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourPlannerApplication extends Application {
    // business layer
    private final TourManager tourManager;

    // view models:
    private final MainViewModel mainViewModel;
    private final TourListViewModel tourListViewModel;
    private final ViewTourViewModel viewTourViewModel;
    private final EditTourViewModel editTourViewModel;

    public TourPlannerApplication() {
        tourManager = new TourManager();

        tourListViewModel = new TourListViewModel(tourManager);
        viewTourViewModel = new ViewTourViewModel(tourManager);
        editTourViewModel = new EditTourViewModel(tourManager);
        mainViewModel = new MainViewModel(tourManager, tourListViewModel, viewTourViewModel, editTourViewModel);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = loadRootNode(mainViewModel, tourListViewModel, viewTourViewModel, editTourViewModel);
        showStage(stage, root);
    }

    public static Parent loadRootNode(MainViewModel mainViewModel, TourListViewModel tourListViewModel, ViewTourViewModel viewTourViewModel, EditTourViewModel editTourViewModel) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main-view.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> {
            if (controllerClass == MainController.class) {
                return new MainController(mainViewModel, viewTourViewModel, editTourViewModel);
            } else if (controllerClass == TourListController.class) {
                return new TourListController(tourListViewModel);
            } else if (controllerClass == ViewTourController.class) {
                return new ViewTourController(viewTourViewModel);
            } else {
                throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
            }
        });

        return fxmlLoader.load();
    }

    public static void showStage(Stage stage, Parent root) {
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Tour Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
