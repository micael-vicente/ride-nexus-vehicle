package pt.ridenexus.vehicle.persistence;

import org.springframework.stereotype.Repository;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.services.VehicleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class NoDBRepository implements VehicleRepository {

    private static Long idSequence = 0L;
    private static final Map<Long, Vehicle> VEHICLES = new HashMap<>();

    @Override
    public Vehicle addVehicle(Vehicle v) {
        Long id = ++idSequence;
        v.setId(id);
        VEHICLES.put(id, v);
        return VEHICLES.get(id);
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle v) {
        if(VEHICLES.containsKey(id)) {
            v.setId(id);
            VEHICLES.put(id, v);
            return VEHICLES.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Long removeVehicle(Long id) {
        return Optional.ofNullable(VEHICLES.remove(id))
            .map(Vehicle::getId)
            .orElse(null);
    }

    @Override
    public List<Vehicle> getVehicles() {
        return VEHICLES.values().stream().toList();
    }

    @Override
    public Vehicle getVehicle(Long id) {
        return VEHICLES.get(id);
    }
}
