package pt.ridenexus.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.web.api.VehicleDto;
import pt.ridenexus.vehicle.web.api.VehicleMutationDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface Service2WebVehicleMapper {

    @Mapping(target = "id", ignore = true)
    Vehicle map(VehicleMutationDto source);

    VehicleDto map(Vehicle source);

    List<VehicleDto> map(List<Vehicle> source);
}
