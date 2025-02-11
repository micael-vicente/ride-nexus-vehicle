package pt.ridenexus.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import pt.ridenexus.vehicle.persistence.model.TrailerEntity;
import pt.ridenexus.vehicle.services.trailer.Trailer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface Service2PersistenceTrailerMapper {

    TrailerEntity map(Trailer source);

    Trailer map(TrailerEntity source);

    void update(Trailer source, @MappingTarget TrailerEntity target);
}
