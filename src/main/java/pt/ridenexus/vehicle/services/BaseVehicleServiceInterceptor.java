
package pt.ridenexus.vehicle.services;

import org.springframework.stereotype.Service;

@Service
public class BaseVehicleServiceInterceptor implements VehicleServiceInterceptor {
    @Override
    public void beforeAddVehicle(Vehicle v) {

    }

    @Override
    public void beforeUpdateVehicle(Vehicle v) {

    }

    @Override
    public void afterAddVehicle(Vehicle v) {

    }

    @Override
    public void afterUpdateVehicle(Vehicle v) {

    }

    @Override
    public void beforeRemoveVehicle(Long id) {

    }

    @Override
    public void afterRemoveVehicle() {

    }
}
