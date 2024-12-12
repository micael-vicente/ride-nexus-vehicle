package pt.ridenexus.vehicle.services;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private Long id;
    private String alias;
    private String countryCode;
    private String licensePlate;
    private LocalDate licensePlateDate;
    private double weight;
}
