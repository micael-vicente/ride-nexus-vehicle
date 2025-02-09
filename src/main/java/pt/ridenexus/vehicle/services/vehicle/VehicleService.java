package pt.ridenexus.vehicle.services.vehicle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pt.ridenexus.vehicle.services.exception.VehicleExistsException;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository repo;

    /**
     * Adds a new vehicle if it does not already exist.
     * A vehicle exists if the combo Country, Region, License Plate has already been used.
     *
     * @param v the vehicle to be added
     * @return the vehicle after being added
     */
    public Vehicle addVehicle(Vehicle v) {

        boolean vehicleExists = repo.vehicleExists(v.getCountryCode(), v.getRegion(), v.getLicensePlate());

        log.info("Persisting vehicle with license plate: {}", v.getLicensePlate());
        if(vehicleExists) {
            log.info("Vehicle already exists for Country: {}, Region: {}, Plate: {}", v.getCountryCode(), v.getRegion(), v.getLicensePlate());
            throw new VehicleExistsException();
        }
        Vehicle vehicle = repo.addVehicle(v);
        log.info("Vehicle with license plate: {} has been persisted", v.getLicensePlate());

        return vehicle;
    }

    /**
     * Updates a vehicle by merging the input object into the existing one.
     *
     * @param id the id of the existing vehicle
     * @param v the input object
     * @return the vehicle after being updated
     */
    public Vehicle updateVehicle(Long id, Vehicle v) {

        log.info("Updating vehicle with license plate: {}", v.getLicensePlate());
        Vehicle vehicle = repo.updateVehicle(id, v);
        log.info("Vehicle with license plate: {} has been updated", v.getLicensePlate());

        return vehicle;
    }

    /**
     * Removes vehicle with given id.
     *
     * @param id the id of the vehicle to be removed
     * @return the id of the vehicle removed
     */
    public Long removeVehicle(Long id) {

        log.info("Removing vehicle with id: {}", id);
        Long removedId = repo.removeVehicle(id);
        log.info("Vehicle with id: {} has been removed", id);

        return removedId;
    }

    /**
     * Gets a vehicle by id.
     *
     * @param id the id of the vehicle to fetch
     * @return the vehicle if found, otherwise null
     */
    public Vehicle getVehicle(Long id) {
        log.info("Getting vehicle with id: {}", id);
        return repo.getVehicle(id);
    }

    /**
     * Returns all vehicles.
     *
     * @return all persisted vehicles
     */
    public Page<Vehicle> getVehicles(int pageNumber, int pageSize) {
        log.info("Getting all vehicles");
        return repo.getVehicles(pageNumber, pageSize);
    }

    /**
     * Returns all vehicles.
     *
     * @return all persisted vehicles
     */
    public Page<Vehicle> getVehiclesByOwner(String ownerId, int pageNumber, int pageSize) {
        log.info("Getting all vehicles with owner: {}", ownerId);
        return repo.getVehiclesByOwner(ownerId, pageNumber, pageSize);
    }
}
