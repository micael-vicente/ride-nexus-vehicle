package pt.ridenexus.vehicle.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "vehicle")
@NoArgsConstructor
public class VehicleEntity extends BaseEntity {

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "version")
    private String version;

    @Column(name = "alias")
    private String alias;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "region")
    private String region;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "license_plate_date", nullable = false)
    private LocalDate licensePlateDate;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "mileage", nullable = false)
    private Integer mileage;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = getEffectiveClass(o);
        Class<?> thisEffectiveClass = getEffectiveClass(this);
        if (thisEffectiveClass != oEffectiveClass) return false;
        VehicleEntity vehicle = (VehicleEntity) o;
        return getId() != null && Objects.equals(getId(), vehicle.getId());
    }

    @Override
    public final int hashCode() {
        return getEffectiveClass(this).hashCode();
    }
}
