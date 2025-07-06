package at.technikumwien.tourplannerbackend.repository;

import at.technikumwien.tourplannerbackend.model.TourItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TourItemRepository extends JpaRepository<TourItem, String> {
    @Modifying
    @Query(value = "INSERT INTO tour_items (id, name, description, start_location_name, start_location_lat, start_location_lng, " +
            "end_location_name, end_location_lat, end_location_lng, transport_type, distance, estimated_time, route_information) " +
            "VALUES (:id, :name, :description, :startLocationName, :startLocationLat, :startLocationLng, " +
            ":endLocationName, :endLocationLat, :endLocationLng, :transportType, :distance, :estimatedTime, :routeInformation)",
            nativeQuery = true)
    void forceInsertTourItem(@Param("id") String id,
                        @Param("name") String name,
                        @Param("description") String description,
                        @Param("startLocationName") String startLocationName,
                        @Param("startLocationLat") double startLocationLat,
                        @Param("startLocationLng") double startLocationLng,
                        @Param("endLocationName") String endLocationName,
                        @Param("endLocationLat") double endLocationLat,
                        @Param("endLocationLng") double endLocationLng,
                        @Param("transportType") String transportType,
                        @Param("distance") Double distance,
                        @Param("estimatedTime") Double estimatedTime,
                        @Param("routeInformation") String routeInformation);
}
