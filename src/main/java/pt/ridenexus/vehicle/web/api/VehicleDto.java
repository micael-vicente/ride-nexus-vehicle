package pt.ridenexus.vehicle.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    private Long id;
    private String region;
    private String make;
    private String model;
    private String version;
    private String alias;
    private String countryCode;
    private String licensePlate;
    private LocalDate licensePlateDate;
    private Double weight;
    private Integer mileage;
}
