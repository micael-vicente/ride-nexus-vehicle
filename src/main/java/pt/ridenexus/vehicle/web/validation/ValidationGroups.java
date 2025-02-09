package pt.ridenexus.vehicle.web.validation;

import jakarta.validation.groups.Default;

public class ValidationGroups {
    public interface AddVehicle extends Default {}
    public interface UpdateVehicle extends Default {}
    public interface AddTrailer extends Default {}
    public interface UpdateTrailer extends Default {}
}
