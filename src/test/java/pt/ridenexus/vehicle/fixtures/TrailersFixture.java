package pt.ridenexus.vehicle.fixtures;

import pt.ridenexus.vehicle.persistence.model.TrailerEntity;
import pt.ridenexus.vehicle.services.trailer.Trailer;
import pt.ridenexus.vehicle.web.api.TrailerDto;

public class TrailersFixture {

    public static Trailer trailer(Long id, String country, String licensePlate) {
        return Trailer.builder()
            .id(id)
            .countryCode(country)
            .licensePlate(licensePlate)
            .ownerId("1")
            .build();
    }

    public static TrailerDto trailerDto(Long id, String country, String licensePlate) {
        return TrailerDto.builder()
            .id(id)
            .countryCode(country)
            .licensePlate(licensePlate)
            .maximumFreightWeight(15000.0)
            .trailerType("REEFER_VAN")
            .status("IN_USE")
            .ownerId("1")
            .build();
    }

    public static TrailerEntity trailerEntity(Long id, String country, String licensePlate) {
        TrailerEntity entity = new TrailerEntity();
        entity.setId(id);
        entity.setCountryCode(country);
        entity.setTrailerType("REEFER_VAN");
        entity.setLicensePlate(licensePlate);

        return entity;
    }
}
