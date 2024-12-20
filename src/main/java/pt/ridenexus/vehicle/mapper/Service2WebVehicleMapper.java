package pt.ridenexus.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.web.api.PageDto;
import pt.ridenexus.vehicle.web.api.VehicleDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface Service2WebVehicleMapper {

    @Mapping(target = "id", ignore = true)
    Vehicle map(VehicleDto source);

    VehicleDto map(Vehicle source);

    @Mapping(target = "pageInfo.pageNumber", source = "number")
    @Mapping(target = "pageInfo.pageSize", source = "size")
    @Mapping(target = "pageInfo.totalElements", source = "totalElements")
    @Mapping(target = "pageInfo.numberOfElements", source = "numberOfElements")
    @Mapping(target = "pageInfo.totalPages", source = "totalPages")
    PageDto<VehicleDto> map(Page<VehicleDto> source);
}
