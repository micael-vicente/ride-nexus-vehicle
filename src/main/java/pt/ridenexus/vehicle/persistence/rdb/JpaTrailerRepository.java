package pt.ridenexus.vehicle.persistence.rdb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ridenexus.vehicle.persistence.model.TrailerEntity;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;

@Repository
public interface JpaTrailerRepository extends JpaRepository<TrailerEntity, Long> {

    boolean existsByCountryCodeAndRegionAndLicensePlate(String countryCode, String region, String licensePlate);

    Page<TrailerEntity> findAllByOwnerId(String ownerId, Pageable pageable);

}
