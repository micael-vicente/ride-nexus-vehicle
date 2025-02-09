package pt.ridenexus.vehicle.services.trailer;

import org.springframework.data.domain.Page;

public interface TrailerRepository {

    /**
     * Adds a vehicle to the repository.
     *
     * @param t the vehicle to be added
     * @return the vehicle after being persisted
     */
    Trailer addTrailer(Trailer t);

    /**
     * Updates a persisted vehicle by merging only changed fields.
     *
     * @param id the id of the persisted vehicle
     * @param v the object containing fields to be updated
     * @return the newly updated and persisted vehicle
     */
    Trailer updateTrailer(Long id, Trailer v);

    /**
     * Removes a vehicle by id.
     *
     * @param id the id of the vehicle to be removed
     * @return the id that has been removed
     */
    Long removeTrailer(Long id);

    /**
     * Gets vehicles in a paginated manner.
     *
     * @param pageNumber page number
     * @param pageSize elements per page
     * @return results in a paginated manner
     */
    Page<Trailer> getTrailers(int pageNumber, int pageSize);

    /**
     * Gets all vehicles belonging to given owner.
     * @param ownerId the id of the owner
     * @param pageNumber the number of the page
     * @param pageSize the number of elements per page
     * @return a paginated result set
     */
    Page<Trailer> getTrailersByOwner(String ownerId,int pageNumber, int pageSize);

    /**
     * Gets a vehicle by id.
     *
     * @param id the id of the vehicle to get
     * @return the vehicle if found null otherwise
     */
    Trailer getTrailer(Long id);

    /**
     * Checks whether a vehicle already exists for given criteria.
     *
     * @param country the country of the vehicle
     * @param region the region of the vehicle
     * @param licensePlate the license plate of the vehicle
     * @return true if found
     */
    boolean trailerExists(String country, String region, String licensePlate);
}
