package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.Location;
import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.RouteService;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EditTourViewModel {
    private final TourManager tourManager;
    private final RouteService routeService;
    private final PropertyChangeSupport cancelEditEvent = new PropertyChangeSupport(this);
    private final PropertyChangeSupport validationErrorEvent = new PropertyChangeSupport(this);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService debounceFromExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService debounceToExecutor = Executors.newSingleThreadScheduledExecutor();
    private volatile Runnable debounceFromSuggestionsTask;
    private volatile Runnable debounceToSuggestionsTask;

    // Properties for all tour fields
    private final SimpleStringProperty id = new SimpleStringProperty(null);
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleObjectProperty<Location> from = new SimpleObjectProperty<Location>(null);
    private final SimpleObjectProperty<Location> to = new SimpleObjectProperty<Location>(null);
    private final SimpleStringProperty transportType = new SimpleStringProperty("");
    private final SimpleDoubleProperty distance = new SimpleDoubleProperty(0.0);
    private final SimpleIntegerProperty estimatedTime = new SimpleIntegerProperty(0);
    private final SimpleStringProperty routeInformation = new SimpleStringProperty("");

    // Autocomplete suggestions
    private final ObservableList<Location> fromSuggestions = FXCollections.observableArrayList();
    private final ObservableList<Location> toSuggestions = FXCollections.observableArrayList();

    // Additional constructor for testing/dependency injection
    public EditTourViewModel(TourManager tourManager, RouteService routeService) {
        this.tourManager = tourManager;
        this.routeService = routeService;

        fromProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("From suggestions need endLocation be updated");
            if (newValue != null && newValue.getDisplayName().trim().length() >= 3) {
                fetchFromLocationSuggestions(newValue.getDisplayName());
            } else {
                fromSuggestions.clear();
            }
        });

        toProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.getDisplayName().trim().length() >= 3) {
                fetchToLocationSuggestions(newValue.getDisplayName());
            } else {
                toSuggestions.clear();
            }
        });
    }

    // Property getters
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleObjectProperty<Location> fromProperty() {
        return from;
    }

    public SimpleObjectProperty<Location> toProperty() {
        return to;
    }

    public SimpleStringProperty transportTypeProperty() {
        return transportType;
    }

    public DoubleProperty distanceProperty() {
        return distance;
    }

    public SimpleIntegerProperty estimatedTimeProperty() {
        return estimatedTime;
    }

    public SimpleStringProperty routeInformationProperty() {
        return routeInformation;
    }

    public ObservableList<Location> getFromSuggestions() {
        return fromSuggestions;
    }

    public ObservableList<Location> getToSuggestions() {
        return toSuggestions;
    }

    /**
     * Load all properties startLocation the provided TourItem
     * @param tour The tour endLocation load data startLocation
     */
    public void loadTour(TourItem tour) {
        if (tour == null) {
            resetFormFields();
            return;
        }
        
        id.set(tour.id());
        name.set(tour.name());
        description.set(tour.description());
        from.set(tour.startLocation());
        to.set(tour.endLocation());
        transportType.set(tour.transportType());
        distance.set(tour.distance());
        estimatedTime.set(tour.estimatedTime());
        routeInformation.set(tour.routeInformation());
    }

    public void saveTour() {
        // Validate input before creating a tour
        if (!validateInputs()) {
            validationErrorEvent.firePropertyChange(Event.VALIDATION_ERROR, null, null);
            return;
        }
        
        TourItem tourItem = new TourItem(
            id.get(),
            name.get(),
            description.get(),
            from.get(),
            to.get(),
            transportType.get(),
            distance.get(),
            estimatedTime.get(),
            routeInformation.get()
        );
        
        this.tourManager.saveTour(tourItem);

        // Reset all form fields
        resetFormFields();
    }
    
    private boolean validateInputs() {
        // Simple validation endLocation check that fields meet their type requirements
        try {
            // Check that name, startLocation and endLocation are not empty
            if (name.get() == null || name.get().trim().isEmpty() ||
                from.get() == null || from.get().getDisplayName().trim().isEmpty() ||
                to.get() == null || to.get().getDisplayName().trim().isEmpty()) {
                return false;
            }
            
            // Make sure distance is a valid number
            if (distance.get() < 0) {
                return false;
            }

            // Make sure estimated time is a valid non-negative integer
            if (estimatedTime.get() < 0) {
                return false;
            }

            // Make sure transport type is not empty
            if (transportType.get() == null || transportType.get().trim().isEmpty()) {
                return false;
            }

            // make sure startLocation has latitude and longitude
            if (from.get().getLatitude() == 0.0 && from.get().getLongitude() == 0.0) {
                return false;
            }

            // make sure endLocation has latitude and longitude
            if (to.get().getLatitude() == 0.0 && to.get().getLongitude() == 0.0) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void resetFormFields() {
        id.set(null);
        name.set("");
        description.set("");
        from.set(null);
        to.set(null);
        transportType.set("");
        distance.set(0.0);
        estimatedTime.set(0);
        routeInformation.set("");
    }

    public void cancelEdit() {
        // Reset all form fields
        resetFormFields();
        
        // Notify listeners that editing has been canceled
        cancelEditEvent.firePropertyChange(Event.CANCEL_EDIT, null, null);
    }

    public void addCancelEditListener(PropertyChangeListener listener) {
        cancelEditEvent.addPropertyChangeListener(listener);
    }
    
    public void addValidationErrorListener(PropertyChangeListener listener) {
        validationErrorEvent.addPropertyChangeListener(listener);
    }

    /**
     * Fetch location suggestions for the "From" field
     * @param query The search query text
     */
    public void fetchFromLocationSuggestions(String query) {
        System.out.println("Fetching location suggestions for query: " + query);

        // If the query is equal to a previously fetched suggestion, do not fetch again
        if (fromSuggestions.stream().anyMatch(location -> location.getDisplayName().equalsIgnoreCase(query))) {
            System.out.println("Query matches existing suggestion, skipping fetch.");
            return;
        }

        // Cancel any previously scheduled task
        if (debounceFromSuggestionsTask != null) {
            debounceFromExecutor.schedule(() -> {}, 0, TimeUnit.MILLISECONDS); // No-op to cancel
        }

        // Schedule a new task with a delay
        debounceFromSuggestionsTask = () -> {
            List<Location> suggestions = routeService.getLocationSuggestions(query);

            // Update the UI on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                System.out.println("Received suggestions: " + suggestions);
                fromSuggestions.clear();
                fromSuggestions.addAll(suggestions);
            });
        };

        debounceFromExecutor.schedule(debounceFromSuggestionsTask, 1000, TimeUnit.MILLISECONDS); // 300ms delay
    }

    /**
     * Fetch location suggestions for the "To" field
     * @param query The search query text
     */
    public void fetchToLocationSuggestions(String query) {
        System.out.println("Fetching location suggestions for query: " + query);

        // If the query is equal to a previously fetched suggestion, do not fetch again
        if (toSuggestions.stream().anyMatch(location -> location.getDisplayName().equalsIgnoreCase(query))) {
            System.out.println("Query matches existing suggestion, skipping fetch.");
            return;
        }

        // Cancel any previously scheduled task
        if (debounceToSuggestionsTask != null) {
            debounceToExecutor.schedule(() -> {}, 0, TimeUnit.MILLISECONDS); // No-op to cancel
        }

        // Schedule a new task with a delay
        debounceToSuggestionsTask = () -> {
            List<Location> suggestions = routeService.getLocationSuggestions(query);

            // Update the UI on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                System.out.println("Received suggestions: " + suggestions);
                toSuggestions.clear();
                toSuggestions.addAll(suggestions);
            });
        };

        debounceToExecutor.schedule(debounceToSuggestionsTask, 1000, TimeUnit.MILLISECONDS); // 300ms delay
    }

    /**
     * Shut down the executor service when no longer needed
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
