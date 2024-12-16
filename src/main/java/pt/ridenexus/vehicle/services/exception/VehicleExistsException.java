package pt.ridenexus.vehicle.services.exception;

public class VehicleExistsException extends RuntimeException {

    public VehicleExistsException() {
        super("Vehicle already exists");
    }
}
