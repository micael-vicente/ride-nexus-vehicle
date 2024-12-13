package pt.ridenexus.vehicle.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;
import pt.ridenexus.vehicle.services.Vehicle;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface Service2PersistenceVehicleMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    VehicleEntity map(Vehicle source);

    List<Vehicle> map(List<VehicleEntity> source);

    Vehicle map(VehicleEntity source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Vehicle source, @MappingTarget VehicleEntity target);
}
