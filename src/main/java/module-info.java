module at.technikumwien.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.technikumwien.tourplanner to javafx.fxml;
    exports at.technikumwien.tourplanner;
}