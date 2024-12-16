package pt.ridenexus.vehicle.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity(name = "vehicle")
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class VehicleEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "version")
    private String version;

    @Column(name = "alias")
    private String alias;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "region")
    private String region;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "license_plate_date")
    private LocalDate licensePlateDate;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "mileage")
    private Integer mileage;
}
