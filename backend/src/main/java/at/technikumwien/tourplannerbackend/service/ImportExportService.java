package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.LogItemRepository;
import at.technikumwien.tourplannerbackend.repository.TourItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ImportExportService {
    private final TourItemRepository tourItemRepository;
    private final LogItemRepository logItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(ImportExportService.class);

    @Autowired
    public ImportExportService(TourItemRepository tourItemRepository, LogItemRepository logItemRepository) {
        this.tourItemRepository = tourItemRepository;
        this.logItemRepository = logItemRepository;
    }

    /**
     * Creates an export file containing all tours and logs in CSV format
     * @return byte array containing the export data
     */
    public byte[] generateExportFile() {
        logger.info("Starting export file generation");
        StringBuilder exportContent = new StringBuilder();

        // Add header row
        exportContent.append("Type,Data\n");

        // Add all tour items
        List<TourItem> tours = tourItemRepository.findAll();
        logger.info("Exporting {} tours", tours.size());
        for (TourItem tour : tours) {
            exportContent.append(tour.getExportString()).append("\n");
        }

        // Add all log items
        List<LogItem> logs = logItemRepository.findAll();
        logger.info("Exporting {} logs", logs.size());
        for (LogItem log : logs) {
            exportContent.append(log.getExportString()).append("\n");
        }

        // Convert to byte array
        byte[] result = exportContent.toString().getBytes(StandardCharsets.UTF_8);
        logger.info("Export file generation completed successfully. Generated {} bytes", result.length);
        return result;
    }
}
