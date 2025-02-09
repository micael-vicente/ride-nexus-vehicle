package pt.ridenexus.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import pt.ridenexus.vehicle.services.trailer.Trailer;
import pt.ridenexus.vehicle.web.api.PageDto;
import pt.ridenexus.vehicle.web.api.TrailerDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface Service2WebTrailerMapper {

    @Mapping(target = "id", ignore = true)
    Trailer map(TrailerDto source);

    TrailerDto map(Trailer source);

    @Mapping(target = "pageInfo.pageNumber", source = "number")
    @Mapping(target = "pageInfo.pageSize", source = "size")
    @Mapping(target = "pageInfo.totalElements", source = "totalElements")
    @Mapping(target = "pageInfo.numberOfElements", source = "numberOfElements")
    @Mapping(target = "pageInfo.totalPages", source = "totalPages")
    PageDto<TrailerDto> map(Page<TrailerDto> source);
}
