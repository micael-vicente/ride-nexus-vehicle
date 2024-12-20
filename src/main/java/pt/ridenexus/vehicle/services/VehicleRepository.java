package pt.ridenexus.vehicle.services;

import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleRepository {

    /**
     * Adds a vehicle to the repository.
     *
     * @param v the vehicle to be added
     * @return the vehicle after being persisted
     */
    Vehicle addVehicle(Vehicle v);

    /**
     * Updates a persisted vehicle by merging only changed fields.
     *
     * @param id the id of the persisted vehicle
     * @param v the object containing fields to be updated
     * @return the newly updated and persisted vehicle
     */
    Vehicle updateVehicle(Long id, Vehicle v);

    /**
     * Removes a vehicle by id.
     *
     * @param id the id of the vehicle to be removed
     * @return the id that has been removed
     */
    Long removeVehicle(Long id);

    /**
     * Gets all vehicles.
     *
     * @return a list of all persisted vehicles
     */
    List<Vehicle> getVehicles();

    /**
     * Gets vehicles in a paginated manner.
     *
     * @param pageNumber page number
     * @param pageSize elements per page
     * @return results in a paginated manner
     */
    Page<Vehicle> getVehicles(int pageNumber, int pageSize);

    /**
     * Gets a vehicle by id.
     *
     * @param id the id of the vehicle to get
     * @return the vehicle if found null otherwise
     */
    Vehicle getVehicle(Long id);

    /**
     * Checks whether a vehicle already exists for given criteria.
     *
     * @param country the country of the vehicle
     * @param region the region of the vehicle
     * @param licensePlate the license plate of the vehicle
     * @return true if found
     */
    boolean vehicleExists(String country, String region, String licensePlate);
}
