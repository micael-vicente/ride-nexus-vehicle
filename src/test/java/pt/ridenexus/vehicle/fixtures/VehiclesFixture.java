package pt.ridenexus.vehicle.fixtures;

import pt.ridenexus.vehicle.services.vehicle.Vehicle;
import pt.ridenexus.vehicle.web.api.VehicleDto;

import java.time.LocalDate;

import static pt.ridenexus.vehicle.services.vehicle.VehicleStatus.BUSY;
import static pt.ridenexus.vehicle.services.vehicle.VehicleType.LIGHT_VEHICLE;

public class VehiclesFixture {

    public static Vehicle vehicle(Long id, String country, String licensePlate) {
        return Vehicle.builder()
            .id(id)
            .countryCode(country)
            .licensePlate(licensePlate)
            .status(BUSY)
            .ownerId("1")
            .vehicleType(LIGHT_VEHICLE)
            .licensePlateDate(LocalDate.now())
            .weight(1000)
            .mileage(1)
            .build();
    }

    public static VehicleDto vehicleDto(Long id, String country, String licensePlate) {
        return VehicleDto.builder()
            .id(id)
            .countryCode(country)
            .licensePlate(licensePlate)
            .status("BUSY")
            .ownerId("1")
            .vehicleType("MOTORCYCLE")
            .licensePlateDate(LocalDate.now().toString())
            .weight(1000)
            .mileage(1)
            .build();
    }
}
