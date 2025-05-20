package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import javafx.beans.property.*;

public class MainViewModel {
    private final TourManager tourManager;
    private final LogManager logManager;
    private final TourListViewModel tourListViewModel;
    private final ViewTourViewModel viewTourViewModel;
    private final EditTourViewModel editTourViewModel;
    private final LogListViewModel logListViewModel;
    private final EditLogViewModel editLogViewModel;
    private final SimpleStringProperty view = new SimpleStringProperty("viewTour");

    public MainViewModel(TourManager tourManager, TourListViewModel tourListViewModel, ViewTourViewModel viewTourViewModel, EditTourViewModel editTourViewModel, LogListViewModel logListViewModel, EditLogViewModel editLogViewModel, LogManager logManager) {
        this.tourManager = tourManager;
        this.tourListViewModel = tourListViewModel;
        this.viewTourViewModel = viewTourViewModel;
        this.editTourViewModel = editTourViewModel;
        this.logListViewModel = logListViewModel;
        this.editLogViewModel = editLogViewModel;
        this.logManager = logManager;


        tourListViewModel.addTourSelectedListener(evt -> {
            if (evt.getPropertyName().equals(Event.TOUR_SELECTED)) {
                TourItem newTourItem = (TourItem) evt.getNewValue();
                viewTourViewModel.currentTourProperty().set(newTourItem);
                setView("viewTour");
            }
        });

        tourListViewModel.addCreateNewTourListener(evt -> {
            if (evt.getPropertyName().equals(Event.EDIT_TOUR)) {
                setView("editTour");
            }
        });

        editTourViewModel.addCancelEditListener(evt -> {
            if (evt.getPropertyName().equals(Event.CANCEL_EDIT)) {
                setView("viewTour");
            }
        });
        
        viewTourViewModel.addEditTourListener(evt -> {
            if (evt.getPropertyName().equals(Event.EDIT_TOUR)) {
                TourItem tourToEdit = (TourItem) evt.getNewValue();
                // Use the new loadTour method to update all properties at once
                editTourViewModel.loadTour(tourToEdit);
                setView("editTour");
            }
        });

        logListViewModel.addCreateNewLogListener(evt -> {
            TourItem currentTour = viewTourViewModel.currentTourProperty().get();
            if (currentTour == null) {
                editLogViewModel.showAlert("Invalid Input", "Please select a tour first");
                return;
            }
            else if (evt.getPropertyName().equals(Event.CREATE_LOG)) {
                LogItem newLog = (LogItem) evt.getNewValue();
                editLogViewModel.loadLog(newLog);
                editLogViewModel.prepareNewLogForTour(currentTour.id());
            }
            else if (evt.getPropertyName().equals(Event.EDIT_LOG)) {
                LogItem existingLog = (LogItem) evt.getNewValue();
                editLogViewModel.loadLog(existingLog);
            }
            setView("editLog");
        });

        editLogViewModel.addCancelLogEditListener(evt -> {
            if (evt.getPropertyName().equals(Event.CANCEL_LOG)) {
                setView("viewTour");
            }
        });
    }

    // get view
    public String getView() {
        return view.get();
    }

    // get view property
    public StringProperty viewProperty() {
        return view;
    }

    private void setView(String view) {
        this.view.set(view);
    }
}
