module at.technikumwien.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires javafx.web;

    opens at.technikumwien.tourplanner to javafx.fxml;
    exports at.technikumwien.tourplanner;
    exports at.technikumwien.tourplanner.viewmodel;
    opens at.technikumwien.tourplanner.viewmodel to javafx.fxml;
    exports at.technikumwien.tourplanner.model;
    opens at.technikumwien.tourplanner.model to javafx.fxml;
    exports at.technikumwien.tourplanner.view;
    opens at.technikumwien.tourplanner.view to javafx.fxml;
}
