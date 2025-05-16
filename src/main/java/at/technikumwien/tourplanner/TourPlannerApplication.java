package at.technikumwien.tourplanner;

import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.view.MainController;
import at.technikumwien.tourplanner.view.TourViewController;
import at.technikumwien.tourplanner.viewmodel.MainViewModel;
import at.technikumwien.tourplanner.viewmodel.TourViewModel;
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
    private final TourViewModel tourViewModel;

    public TourPlannerApplication() {
        tourManager = new TourManager();

        tourViewModel = new TourViewModel();
        mainViewModel = new MainViewModel(tourManager, tourViewModel);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = loadRootNode(mainViewModel, tourViewModel);
        showStage(stage, root);
    }

    public static Parent loadRootNode(MainViewModel mainViewModel, TourViewModel tourViewModel) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main-view.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> {
            if (controllerClass == MainController.class) {
                return new MainController(mainViewModel);
            } else if (controllerClass == TourViewController.class) {
                return new TourViewController(tourViewModel);
            } else {
                throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
            }
        });

        return fxmlLoader.load();
    }

    public static void showStage(Stage stage, Parent root) {
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Tour Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
