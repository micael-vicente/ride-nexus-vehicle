package pt.ridenexus.vehicle.services.trailer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trailer {

    private Long id;
    private String ownerId;
    private String countryCode;
    private String licensePlate;
    private String trailerType;
    private String status;
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
