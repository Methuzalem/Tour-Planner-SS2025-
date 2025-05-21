package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
class EditLogViewModelTest {
    private LogManager logManager;
    private EditLogViewModel viewModel;

    @BeforeEach
    void setUp() {
        logManager = mock(LogManager.class);
        viewModel = new EditLogViewModel(logManager);
    }

    @Test
    void testLoadLog_setsAllFieldsCorrectly() {
        LogItem logItem = new LogItem(
                "log123",
                "tour789",
                LocalDate.of(2024, 10, 1),
                3.5,
                "Nice tour!",
                "2h 30min",
                "8.5km",
                "5 - best"
        );

        viewModel.loadLog(logItem);

        assertEquals("log123", viewModel.logIdProperty().get());
        assertEquals("tour789", viewModel.tourIdProperty().get());
        assertEquals(LocalDate.of(2024, 10, 1), viewModel.dateProperty().get());
        assertEquals("Nice tour!", viewModel.commentProperty().get());
        assertEquals(3.5, viewModel.difficultyProperty().get());
        assertEquals("2h 30min", viewModel.totalTimeProperty().get());
        assertEquals("8.5km", viewModel.totalDistanceProperty().get());
        assertEquals("5 - best", viewModel.ratingProperty().get());
    }

    @Test
    void testCreateNewLog_savesLogCorrectly_whenAllFieldsValid() {
        // prepare valid form values
        viewModel.dateProperty().set(LocalDate.of(2025, 5, 20));
        viewModel.difficultyProperty().set(2.0);
        viewModel.totalTimeProperty().set("3h");
        viewModel.totalDistanceProperty().set("12km");
        viewModel.commentProperty().set("Test comment");
        viewModel.ratingProperty().set("4 - good");
        viewModel.prepareNewLogForTour("tour456");

        viewModel.createNewLog();

        ArgumentCaptor<LogItem> captor = ArgumentCaptor.forClass(LogItem.class);
        verify(logManager).saveLog(captor.capture());

        LogItem saved = captor.getValue();
        assertNull(saved.getLogId()); // new Log
        assertEquals("tour456", saved.getTourId());
        assertEquals(LocalDate.of(2025, 5, 20), saved.getDate());
        assertEquals(2.0, saved.getDifficulty());
        assertEquals("3h", saved.getTotalTime());
        assertEquals("12km", saved.getTotalDistance());
        assertEquals("Test comment", saved.getComment());
        assertEquals("4 - good", saved.getRating());
    }
}