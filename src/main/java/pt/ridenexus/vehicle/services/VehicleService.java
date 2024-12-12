package pt.ridenexus.vehicle.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleServiceInterceptor interceptor;
    private final VehicleRepository repo;

    public Vehicle addVehicle(Vehicle v) {
        interceptor.beforeAddVehicle(v);

        LOG.info("Persisting vehicle with license plate: {}", v.getLicensePlate());
        Vehicle vehicle = repo.addVehicle(v);
        LOG.info("Vehicle with license plate: {} has been persisted", v.getLicensePlate());

        interceptor.afterAddVehicle(v);

        return vehicle;
    }

    public Vehicle updateVehicle(Long id, Vehicle v) {
        interceptor.beforeUpdateVehicle(v);

        LOG.info("Updating vehicle with license plate: {}", v.getLicensePlate());
        Vehicle vehicle = repo.updateVehicle(id, v);
        LOG.info("Vehicle with license plate: {} has been updated", v.getLicensePlate());

        interceptor.afterUpdateVehicle(v);

        return vehicle;
    }

    public Long removeVehicle(Long id) {
        interceptor.beforeRemoveVehicle(id);

        LOG.info("Removing vehicle with id: {}", id);
        Long removedId = repo.removeVehicle(id);
        LOG.info("Vehicle with id: {} has been removed", id);

        interceptor.afterRemoveVehicle();

        return removedId;
    }

    public Vehicle getVehicle(Long id) {
        LOG.info("Getting vehicle with id: {}", id);
        return repo.getVehicle(id);
    }

    public List<Vehicle> getVehicles() {
        LOG.info("Getting all vehicles");
        return repo.getVehicles();
    }
}
