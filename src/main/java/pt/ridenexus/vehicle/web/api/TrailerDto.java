package pt.ridenexus.vehicle.web.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ridenexus.vehicle.services.trailer.TrailerType;
import pt.ridenexus.vehicle.web.validation.ValidationGroups;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.AddTrailer;
import pt.ridenexus.vehicle.web.validation.annotation.IsEnumMember;
import pt.ridenexus.vehicle.web.validation.annotation.IsKnownCountry;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrailerDto {

    private Long id;

    @NotNull(groups = AddTrailer.class)
    private String ownerId;

    @IsKnownCountry
    @NotBlank(groups = ValidationGroups.AddVehicle.class)
    private String countryCode;

    @NotBlank(groups = ValidationGroups.AddVehicle.class)
    private String licensePlate;

    @NotNull(groups = AddTrailer.class)
    @IsEnumMember(enumClass = TrailerType.class, appendValuesToMessage = true)
    private String trailerType;

    @NotNull(groups = AddTrailer.class)
    private String status;

    @NotNull(groups = AddTrailer.class)
    private Double maximumFreightWeight;

    private String region;
    private Double fdLengthCentimeters;
    private Double fdWidthCentimeters;
    private Double fdHeightCentimeters;
    private Double mdLengthCentimeters;
    private Double mdWidthCentimeters;
    private Double mdHeightCentimeters;
    private Double rdLengthCentimeters;
    private Double rdWidthCentimeters;
    private Double rdHeightCentimeters;
}
