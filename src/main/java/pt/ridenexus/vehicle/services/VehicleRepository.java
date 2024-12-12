package pt.ridenexus.vehicle.services;

import java.util.List;

public interface VehicleRepository {

    Vehicle addVehicle(Vehicle v);

    Vehicle updateVehicle(Long id, Vehicle v);

    Long removeVehicle(Long id);

    List<Vehicle> getVehicles();

    Vehicle getVehicle(Long id);
}
