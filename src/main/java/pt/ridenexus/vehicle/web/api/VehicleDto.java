package pt.ridenexus.vehicle.web.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ridenexus.vehicle.services.vehicle.VehicleStatus;
import pt.ridenexus.vehicle.services.vehicle.VehicleType;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.AddVehicle;
import pt.ridenexus.vehicle.web.validation.annotation.IsEnumMember;
import pt.ridenexus.vehicle.web.validation.annotation.IsKnownCountry;
import pt.ridenexus.vehicle.web.validation.annotation.ValidDateFormat;

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

    @NotBlank(groups = AddVehicle.class)
    private String ownerId;

    @NotNull(groups = AddVehicle.class)
    @IsEnumMember(enumClass = VehicleType.class, appendValuesToMessage = true)
    private String vehicleType;

    @NotNull(groups = AddVehicle.class)
    @IsEnumMember(enumClass = VehicleStatus.class, appendValuesToMessage = true)
    private String status;

    @IsKnownCountry
    @NotBlank(groups = AddVehicle.class)
    private String countryCode;

    @NotBlank(groups = AddVehicle.class)
    private String licensePlate;

    @ValidDateFormat(message = "Invalid date format, use: [yyyy-MM-dd]")
    @NotBlank(groups = AddVehicle.class)
    private String licensePlateDate;

    @Max(value = 99999)
    @Positive
    @NotNull(groups = AddVehicle.class)
    private Integer weight;

    @PositiveOrZero
    @NotNull(groups = AddVehicle.class)
    private Integer mileage;
}
