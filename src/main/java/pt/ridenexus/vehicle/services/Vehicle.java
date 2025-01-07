package pt.ridenexus.vehicle.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private Long id;
    private String region;
    private String make;
    private String model;
    private String version;
    private String alias;
    private String countryCode;
    private String licensePlate;
    private String ownerId;
    private LocalDate licensePlateDate;
    private Integer weight;
    private Integer mileage;
}
