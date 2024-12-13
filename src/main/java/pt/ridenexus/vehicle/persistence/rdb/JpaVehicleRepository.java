package pt.ridenexus.vehicle.persistence.rdb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;

@Repository
public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, Long> {
}
