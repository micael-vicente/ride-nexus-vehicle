package pt.ridenexus.vehicle.persistence.rdb;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import pt.ridenexus.vehicle.mapper.Service2PersistenceVehicleMapper;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.services.VehicleRepository;
import pt.ridenexus.vehicle.services.exception.ObjectNotFoundException;

@Repository
@RequiredArgsConstructor
public class JpaVehicleRepositoryWrapper implements VehicleRepository {

    private final Service2PersistenceVehicleMapper mapper;
    private final JpaVehicleRepository repo;

    @Override
    public Vehicle addVehicle(Vehicle v) {

        VehicleEntity result = repo.save(mapper.map(v));

        return mapper.map(result);
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle v) {
        VehicleEntity vehicle = repo.findById(id).orElse(null);

        if(vehicle == null) {
            throw new ObjectNotFoundException("Vehicle not found. Id: " + id);
        }

        mapper.update(v, vehicle);
        VehicleEntity updated = repo.save(vehicle);

        return mapper.map(updated);
    }

    @Override
    public Long removeVehicle(Long id) {
        repo.deleteById(id);
        return id;
    }

    @Override
    public Page<Vehicle> getVehicles(int pageNumber, int pageSize) {
        return repo.findAll(PageRequest.of(pageNumber, pageSize))
                .map(mapper::map);
    }

    @Override
    public Page<Vehicle> getVehiclesByOwner(String ownerId, int pageNumber, int pageSize) {
        return repo.findAllByOwnerId(ownerId, PageRequest.of(pageNumber, pageSize))
            .map(mapper::map);
    }

    @Override
    public Vehicle getVehicle(Long id) {
        VehicleEntity vehicle = repo.findById(id).orElse(null);

        return mapper.map(vehicle);
    }

    @Override
    public boolean vehicleExists(String country, String region, String licensePlate) {
        return repo.existsByCountryCodeAndRegionAndLicensePlate(country, region, licensePlate);
    }
}
