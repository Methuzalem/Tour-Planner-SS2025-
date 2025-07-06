package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.LogItem;
import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.LogItemRepository;
import at.technikumwien.tourplannerbackend.repository.TourItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

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

    /**
     * Imports tours and logs from a .tpexp file
     * @param fileContent The content of the .tpexp file as a byte array
     * @return The number of items imported
     */
    @Transactional
    public int importFile(byte[] fileContent) {
        logger.info("Starting import process");
        String content = new String(fileContent, StandardCharsets.UTF_8);
        String[] lines = content.split("\n");
        int importedCount = 0;

        // Skip header line if present
        int startLine = 0;
        if (lines.length > 0 && lines[0].startsWith("Type,")) {
            startLine = 1;
            logger.debug("Skipping header line");
        }

        for (int i = startLine; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            try {
                if (line.startsWith("T,")) {
                    // Process tour item
                    TourItem tourItem = TourItem.fromExportString(line);
                    Optional<TourItem> existingTour = tourItemRepository.findById(tourItem.getId());

                    if (existingTour.isPresent()) {
                        logger.info("Updating existing tour: {}", tourItem.getName());
                        tourItemRepository.save(tourItem);
                    } else {
                        logger.info("Creating new tour: {}", tourItem.getName());
                        // Extract all necessary fields from tourItem and pass them individually
                        tourItemRepository.forceInsertTourItem(
                            tourItem.getId(),
                            tourItem.getName(),
                            tourItem.getDescription(),
                            tourItem.getStartLocation() != null ? tourItem.getStartLocation().getDisplayName() : null,
                            tourItem.getStartLocation() != null ? tourItem.getStartLocation().getLatitude() : 0,
                            tourItem.getStartLocation() != null ? tourItem.getStartLocation().getLongitude() : 0,
                            tourItem.getEndLocation() != null ? tourItem.getEndLocation().getDisplayName() : null,
                            tourItem.getEndLocation() != null ? tourItem.getEndLocation().getLatitude() : 0,
                            tourItem.getEndLocation() != null ? tourItem.getEndLocation().getLongitude() : 0,
                            tourItem.getTransportType(),
                            tourItem.getDistance(),
                            (Double) tourItem.getEstimatedTime(),
                            tourItem.getRouteInformation()
                        );
                    }

                    importedCount++;
                } else if (line.startsWith("L,")) {
                    // Process log item
                    LogItem logItem = LogItem.fromExportString(line);
                    Optional<LogItem> existingLog = logItemRepository.findById(logItem.getLogId());

                    if (existingLog.isPresent()) {
                        logger.info("Updating existing log for tour: {}", logItem.getTourId());
                    } else {
                        logger.info("Creating new log for tour: {}", logItem.getTourId());
                    }

                    logItemRepository.save(logItem);
                    importedCount++;
                } else {
                    logger.warn("Skipping unrecognized line format at line {}: {}", i + 1, line);
                }
            } catch (Exception e) {
                logger.error("Error processing line {}: {} - {}", i + 1, e.getMessage(), e);
                e.printStackTrace();
            }
        }

        logger.info("Import completed: {} items imported", importedCount);
        return importedCount;
    }
}
