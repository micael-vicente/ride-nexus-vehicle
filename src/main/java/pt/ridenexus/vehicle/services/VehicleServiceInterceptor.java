package pt.ridenexus.vehicle.services;

public interface VehicleServiceInterceptor {
    void beforeAddVehicle(Vehicle v);

    void beforeUpdateVehicle(Vehicle v);

    void afterAddVehicle(Vehicle v);

    void afterUpdateVehicle(Vehicle v);

    void beforeRemoveVehicle(Long id);

    void afterRemoveVehicle();
}
