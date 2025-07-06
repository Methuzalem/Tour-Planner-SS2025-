package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.repository.LogItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogServiceTest {

    private LogItemRepository repositoryMock;
    private LogService logService;

    @BeforeEach
    public void setUp() {
        repositoryMock = mock(LogItemRepository.class);
        logService = new LogService(repositoryMock);
    }

    @Test
    public void testGetAllLogs_ReturnsList() {
        List<LogItem> mockLogs = List.of(new LogItem(), new LogItem());
        when(repositoryMock.findAll()).thenReturn(mockLogs);

        List<LogItem> result = logService.getAllLogs();

        assertEquals(2, result.size());
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    public void testSaveLog_CallsRepositorySave() {
        LogItem log = new LogItem();
        logService.saveLog(log);

        verify(repositoryMock, times(1)).save(log);
    }

    @Test
    public void testUpdateLog_WhenLogExists_UpdatesFields() {
        LogItem existing = new LogItem();
        existing.setLogId("123");
        existing.setComment("Old");

        LogItem updated = new LogItem();
        updated.setTourId("newTour");
        updated.setDate(LocalDate.of(2024, 1, 1));
        updated.setDifficulty(3.0);
        updated.setComment("New comment");
        updated.setTotalTime(120);
        updated.setRating(5);

        when(repositoryMock.findById("123")).thenReturn(Optional.of(existing));

        logService.updateLog("123", updated);

        assertEquals("New comment", existing.getComment());
        assertEquals("newTour", existing.getTourId());
        verify(repositoryMock).save(existing);
    }

    @Test
    public void testUpdateLog_WhenLogNotFound_ThrowsException() {
        when(repositoryMock.findById("notFound")).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                logService.updateLog("notFound", new LogItem())
        );

        assertTrue(thrown.getMessage().contains("not found"));
        verify(repositoryMock, never()).save(any());
    }

    @Test
    public void testDeleteLog_CallsRepositoryDeleteById() {
        logService.deleteLog("456");

        verify(repositoryMock, times(1)).deleteById("456");
    }
}
