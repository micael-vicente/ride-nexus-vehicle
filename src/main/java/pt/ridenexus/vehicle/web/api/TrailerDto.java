package pt.ridenexus.vehicle.web.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ridenexus.vehicle.services.trailer.TrailerType;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.AddTrailer;
import pt.ridenexus.vehicle.web.validation.annotation.IsEnumMember;
import pt.ridenexus.vehicle.web.validation.annotation.IsKnownCountry;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrailerDto {

    private Long id;
    private String region;

    @NotNull(groups = AddTrailer.class)
    private String ownerId;

    @IsKnownCountry
    @NotBlank(groups = AddTrailer.class)
    private String countryCode;

    @NotBlank(groups = AddTrailer.class)
    private String licensePlate;

    @NotNull(groups = AddTrailer.class)
    @IsEnumMember(enumClass = TrailerType.class, appendValuesToMessage = true)
    private String trailerType;

    @NotNull(groups = AddTrailer.class)
    private String status;

    @Positive
    @NotNull(groups = AddTrailer.class)
    private Double maximumFreightWeight;

    @Positive
    private Double fdLengthCentimeters;

    @Positive
    private Double fdWidthCentimeters;

    @Positive
    private Double fdHeightCentimeters;

    @Positive
    private Double mdLengthCentimeters;

    @Positive
    private Double mdWidthCentimeters;

    @Positive
    private Double mdHeightCentimeters;

    @Positive
    private Double rdLengthCentimeters;

    @Positive
    private Double rdWidthCentimeters;

    @Positive
    private Double rdHeightCentimeters;
}
