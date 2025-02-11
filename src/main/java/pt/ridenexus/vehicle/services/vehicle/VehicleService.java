package pt.ridenexus.vehicle.services.vehicle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pt.ridenexus.vehicle.mapper.Service2PersistenceVehicleMapper;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;
import pt.ridenexus.vehicle.persistence.rdb.VehicleRepository;
import pt.ridenexus.vehicle.services.exception.EntityExistsException;
import pt.ridenexus.vehicle.services.exception.ObjectNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository repo;
    private final Service2PersistenceVehicleMapper mapper;

    /**
     * Adds a new vehicle if it does not already exist.
     * A vehicle exists if the combo Country, Region, License Plate has already been used.
     *
     * @param v the vehicle to be added
     * @return the vehicle after being added
     */
    public Vehicle addVehicle(Vehicle v) {

        boolean vehicleExists = repo.existsByCountryCodeAndRegionAndLicensePlate(v.getCountryCode(), v.getRegion(), v.getLicensePlate());

        log.info("Persisting vehicle with license plate: {}", v.getLicensePlate());
        if(vehicleExists) {
            log.info("Vehicle already exists for Country: {}, Region: {}, Plate: {}", v.getCountryCode(), v.getRegion(), v.getLicensePlate());
            throw new EntityExistsException("Vehicle already exists");
        }

        VehicleEntity saved = repo.save(mapper.map(v));
        Vehicle vehicle = mapper.map(saved);
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
        VehicleEntity vehicle = repo.findById(id).orElse(null);

        if(vehicle == null) {
            throw new ObjectNotFoundException("Vehicle not found. Id: " + id);
        }

        mapper.update(v, vehicle);
        VehicleEntity updated = repo.save(vehicle);

        log.info("Vehicle with license plate: {} has been updated", v.getLicensePlate());

        return mapper.map(updated);
    }

    /**
     * Removes vehicle with given id.
     *
     * @param id the id of the vehicle to be removed
     * @return the id of the vehicle removed
     */
    public Long removeVehicle(Long id) {

        log.info("Removing vehicle with id: {}", id);
        repo.deleteById(id);
        log.info("Vehicle with id: {} has been removed", id);

        return id;
    }

    /**
     * Gets a vehicle by id.
     *
     * @param id the id of the vehicle to fetch
     * @return the vehicle if found, otherwise null
     */
    public Vehicle getVehicle(Long id) {
        log.info("Getting vehicle with id: {}", id);
        VehicleEntity byId = repo.findById(id).orElse(null);
        return mapper.map(byId);
    }

    /**
     * Returns all vehicles.
     *
     * @return all persisted vehicles
     */
    public Page<Vehicle> getVehicles(int pageNumber, int pageSize) {
        log.info("Getting all vehicles");
        return repo.findAll(PageRequest.of(pageNumber, pageSize))
            .map(mapper::map);
    }

    /**
     * Returns all vehicles.
     *
     * @return all persisted vehicles
     */
    public Page<Vehicle> getVehiclesByOwner(String ownerId, int pageNumber, int pageSize) {
        log.info("Getting all vehicles with owner: {}", ownerId);
        return repo.findAllByOwnerId(ownerId, PageRequest.of(pageNumber, pageSize))
            .map(mapper::map);
    }
}
