package pt.ridenexus.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import pt.ridenexus.vehicle.persistence.model.TrailerEntity;
import pt.ridenexus.vehicle.services.trailer.Trailer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface Service2PersistenceTrailerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    TrailerEntity map(Trailer source);

    @Mapping(target = "id", ignore = true)
    Trailer map(TrailerEntity source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void update(Trailer source, @MappingTarget TrailerEntity target);
}
