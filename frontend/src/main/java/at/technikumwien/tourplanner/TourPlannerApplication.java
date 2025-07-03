package at.technikumwien.tourplanner;

import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.service.ReportService;
import at.technikumwien.tourplanner.service.RouteService;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.view.MainController;
import at.technikumwien.tourplanner.view.MapController;
import at.technikumwien.tourplanner.view.TourListController;
import at.technikumwien.tourplanner.view.ViewTourController;
import at.technikumwien.tourplanner.viewmodel.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourPlannerApplication extends Application {
    // business layer
    private final TourManager tourManager;
    private final LogManager logManager;
    private final RouteService routeService;
    private final ReportService reportService;

    // view models:
    private final MainViewModel mainViewModel;
    private final TourListViewModel tourListViewModel;
    private final ViewTourViewModel viewTourViewModel;
    private final EditTourViewModel editTourViewModel;
    private final LogListViewModel logListViewModel;
    private final EditLogViewModel editLogViewModel;

    public TourPlannerApplication() {
        tourManager = new TourManager();
        logManager = new LogManager();
        routeService = new RouteService();
        reportService = new ReportService();

        tourListViewModel = new TourListViewModel(tourManager);
        viewTourViewModel = new ViewTourViewModel(tourManager, reportService);
        editTourViewModel = new EditTourViewModel(tourManager, routeService);
        logListViewModel = new LogListViewModel(logManager);
        editLogViewModel = new EditLogViewModel(logManager);
        mainViewModel = new MainViewModel(tourManager, tourListViewModel, viewTourViewModel, editTourViewModel, logListViewModel, editLogViewModel, logManager);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = loadRootNode(mainViewModel, tourListViewModel, viewTourViewModel, editTourViewModel, logListViewModel, editLogViewModel, logManager);
        showStage(stage, root);
    }

    public static Parent loadRootNode(MainViewModel mainViewModel, TourListViewModel tourListViewModel, ViewTourViewModel viewTourViewModel, EditTourViewModel editTourViewModel, LogListViewModel logListViewModel, EditLogViewModel editLogViewModel, LogManager logManager) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main-view.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> {
            if (controllerClass == MainController.class) {
                return new MainController(mainViewModel, viewTourViewModel, editTourViewModel, tourListViewModel, logListViewModel, editLogViewModel, logManager);
            } else if (controllerClass == TourListController.class) {
                return new TourListController(tourListViewModel);
            }  else {
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
