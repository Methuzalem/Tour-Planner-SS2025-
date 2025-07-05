package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.utils.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class LogManagerTest {

    private LogManager logManager;

    @BeforeEach
    public void setUp() {
        logManager = new LogManager() {
            {
                getLogList().clear();
            }
        };
    }

    @Test
    public void testAddNewLog_AddsToListAndFiresEvent() {
        LogItem newItem = new LogItem(
                null,
                "tour123",
                LocalDate.of(2024, 5, 10),
                3.5,
                "Test-Kommentar",
                "01:20",
                "4"
        );

        AtomicBoolean eventFired = new AtomicBoolean(false);
        logManager.addCreateLogListener(evt -> {
            if (Event.REFRESH_LOG.equals(evt.getPropertyName())) {
                eventFired.set(true);
            }
        });

        logManager.saveLog(newItem);

        ObservableList<LogItem> logs = logManager.getLogList();
        assertFalse(logs.isEmpty());
        assertNotNull(logs.get(0).logId());
        assertEquals("tour123", logs.get(0).tourId());
        assertTrue(eventFired.get());
    }

    @Test
    public void testUpdateLog_UpdatesExistingLog() {
        LogItem existing = new LogItem(
                "log-1",
                "tour456",
                LocalDate.of(2024, 3, 15),
                2.0,
                "Old Comment",
                "00:30",
                "3"
        );
        logManager.getLogList().add(existing);

        LogItem updated = new LogItem(
                "log-1",
                "tour456",
                LocalDate.of(2024, 3, 15),
                2.0,
                "Updated Comment",
                "00:30",
                "3"
        );

        logManager.saveLog(updated);

        LogItem result = logManager.getLogList().get(0);
        assertEquals("Updated Comment", result.comment());
    }

    @Test
    public void testDeleteLog_RemovesFromListAndFiresEvent() {
        LogItem item = new LogItem(
                "log-del",
                "tour789",
                LocalDate.of(2024, 6, 1),
                4.0,
                "Delete me",
                "01:00",
                "5"
        );
        logManager.getLogList().add(item);

        AtomicBoolean eventTriggered = new AtomicBoolean(false);
        logManager.addCreateLogListener(evt -> {
            if (Event.REFRESH_LOG.equals(evt.getPropertyName())) {
                eventTriggered.set(true);
            }
        });

        logManager.deleteLog(item);

        assertTrue(logManager.getLogList().isEmpty());
        assertTrue(eventTriggered.get());
    }

    @Test
    public void testDeleteLog_NullOrMissingId_DoesNothing() {
        logManager.deleteLog(null); // kein Crash
        logManager.deleteLog(new LogItem(null, null, null, 0.00, null, null, null)); // kein Crash
        assertTrue(logManager.getLogList().isEmpty());
    }
}
