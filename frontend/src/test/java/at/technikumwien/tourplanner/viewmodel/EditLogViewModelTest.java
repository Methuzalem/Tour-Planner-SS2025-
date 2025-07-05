package at.technikumwien.tourplanner.viewmodel;
import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.service.LogManager;
import at.technikumwien.tourplanner.utils.Event;
import at.technikumwien.tourplanner.utils.RatingOption;
import at.technikumwien.tourplanner.viewmodel.EditLogViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EditLogViewModelTest {

    private EditLogViewModel viewModel;
    private LogManager logManagerMock;

    @BeforeEach
    public void setUp() {
        logManagerMock = mock(LogManager.class);
        viewModel = new EditLogViewModel(logManagerMock);
    }

    @Test
    public void testLoadLog_PopulatesFields() {
        LogItem log = new LogItem("log1", "tour1", LocalDate.of(2023, 1, 1), 2.5, "Nice tour", 120,  "4");

        viewModel.loadLog(log);

        assertEquals("log1", viewModel.logIdProperty().get());
        assertEquals("tour1", viewModel.tourIdProperty().get());
        assertEquals(LocalDate.of(2023, 1, 1), viewModel.dateProperty().get());
        assertEquals("Nice tour", viewModel.commentProperty().get());
        assertEquals(2.5, viewModel.difficultyProperty().get());
        assertEquals(120, viewModel.totalTimeProperty().get());
        assertEquals("Gut", viewModel.getRating().getLabel());
    }

    @Test
    public void testCancelEditLog_ResetsFieldsAndFiresEvent() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        viewModel.addCancelLogEditListener(listener);

        viewModel.logIdProperty().set("log1");
        viewModel.tourIdProperty().set("tour1");
        viewModel.dateProperty().set(LocalDate.now());

        viewModel.cancelEditLog();

        assertNull(viewModel.logIdProperty().get());
        assertNull(viewModel.tourIdProperty().get());
        assertNull(viewModel.dateProperty().get());
        verify(listener, times(1)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void testCreateNewLog_SuccessfulSave() {

        viewModel.tourIdProperty().set("tour1");
        viewModel.dateProperty().set(LocalDate.of(2024, 12, 1));
        viewModel.difficultyProperty().set(3.0);
        viewModel.commentProperty().set("Well done");
        viewModel.totalTimeProperty().set(120);
        viewModel.ratingProperty().set(new RatingOption("5 - Sehr Gut", 5));

        viewModel.createNewLog();

        ArgumentCaptor<LogItem> captor = ArgumentCaptor.forClass(LogItem.class);
        verify(logManagerMock, times(1)).saveLog(captor.capture());

        LogItem saved = captor.getValue();
        assertEquals("tour1", saved.getTourId());
        assertEquals(120, saved.getTotalTime());
        assertEquals("Well done", saved.getComment());
        assertEquals("5", saved.getRating());
    }

}