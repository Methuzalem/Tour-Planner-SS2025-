package at.technikumwien.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapController {
    @FXML
    private WebView webView;
    private WebEngine webEngine;

    @FXML
    public void initialize() {
        System.out.println("MapController");
        webEngine = webView.getEngine(); // Initialize the WebEngine from the injected WebView

        // Load local HTML file or inline HTML with Leaflet
        webEngine.load(getClass().getResource("/map.html").toExternalForm());

        // Wait for page load and then inject route
        webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                // Example coordinates (route)
                String js = """
                    addRoute([
                        [48.2082, 16.3738],
                        [48.2065, 16.3720],
                        [48.2049, 16.3703]
                    ]);
                """;
                webEngine.executeScript(js);
            }
        });
    }
}
