package pt.ridenexus.vehicle.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "trailer")
@NoArgsConstructor
public class TrailerEntity extends BaseEntity {

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "trailer_type", nullable = false)
    private String trailerType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "region")
    private String region;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "maximum_freight_weight", nullable = false)
    private Double maximumFreightWeight;

    @Column(name = "fd_length_centimeters")
    private Double fdLengthCentimeters;

    @Column(name = "fd_width_centimeters")
    private Double fdWidthCentimeters;

    @Column(name = "fd_height_centimeters")
    private Double fdHeightCentimeters;

    @Column(name = "md_length_centimeters", nullable = false)
    private Double mdLengthCentimeters;

    @Column(name = "md_width_centimeters", nullable = false)
    private Double mdWidthCentimeters;

    @Column(name = "md_height_centimeters", nullable = false)
    private Double mdHeightCentimeters;

    @Column(name = "rd_length_centimeters")
    private Double rdLengthCentimeters;

    @Column(name = "rd_width_centimeters")
    private Double rdWidthCentimeters;

    @Column(name = "rd_height_centimeters")
    private Double rdHeightCentimeters;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = getEffectiveClass(o);
        Class<?> thisEffectiveClass = getEffectiveClass(this);
        if (thisEffectiveClass != oEffectiveClass) return false;
        TrailerEntity that = (TrailerEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return getEffectiveClass(this).hashCode();
    }
}
