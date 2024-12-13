package pt.ridenexus.vehicle.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMutationDto {
    private String alias;
    private String make;
    private String model;
    private String version;
    private String countryCode;
    private String region;
    private String licensePlate;
    private String licensePlateDate;
    private Double weight;
    private Integer mileage;
}
