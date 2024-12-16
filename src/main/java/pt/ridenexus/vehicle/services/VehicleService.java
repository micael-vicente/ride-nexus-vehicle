package pt.ridenexus.vehicle.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ridenexus.vehicle.services.exception.VehicleExistsException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleServiceInterceptor interceptor;
    private final VehicleRepository repo;

    public Vehicle addVehicle(Vehicle v) {
        interceptor.beforeAddVehicle(v);

        boolean vehicleExists = repo.vehicleExists(v.getCountryCode(), v.getRegion(), v.getLicensePlate());

        log.info("Persisting vehicle with license plate: {}", v.getLicensePlate());
        if(vehicleExists) {
            log.info("Vehicle already exists for Country: {}, Region: {}, Plate: {}", v.getCountryCode(), v.getRegion(), v.getLicensePlate());
            throw new VehicleExistsException();
        }
        Vehicle vehicle = repo.addVehicle(v);
        log.info("Vehicle with license plate: {} has been persisted", v.getLicensePlate());

        interceptor.afterAddVehicle(v);

        return vehicle;
    }

    public Vehicle updateVehicle(Long id, Vehicle v) {
        interceptor.beforeUpdateVehicle(v);

        log.info("Updating vehicle with license plate: {}", v.getLicensePlate());
        Vehicle vehicle = repo.updateVehicle(id, v);
        log.info("Vehicle with license plate: {} has been updated", v.getLicensePlate());

        interceptor.afterUpdateVehicle(v);

        return vehicle;
    }

    public Long removeVehicle(Long id) {
        interceptor.beforeRemoveVehicle(id);

        log.info("Removing vehicle with id: {}", id);
        Long removedId = repo.removeVehicle(id);
        log.info("Vehicle with id: {} has been removed", id);

        interceptor.afterRemoveVehicle();

        return removedId;
    }

    public Vehicle getVehicle(Long id) {
        log.info("Getting vehicle with id: {}", id);
        return repo.getVehicle(id);
    }

    public List<Vehicle> getVehicles() {
        log.info("Getting all vehicles");
        return repo.getVehicles();
    }
}
