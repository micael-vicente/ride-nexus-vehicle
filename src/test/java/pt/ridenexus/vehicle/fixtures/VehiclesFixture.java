package pt.ridenexus.vehicle.fixtures;

import java.util.Map;

public class VehiclesFixture {

    public static Map<String, Object> vehicle(String country, String licensePlate) {
        return Map.of(
            "countryCode", country,
            "licensePlate", licensePlate,
            "status", "BUSY",
            "ownerId", "1",
            "vehicleType", "MOTORCYCLE",
            "licensePlateDate", "2024-12-19",
            "weight", 1000,
            "mileage", 1
        );
    }

    public static Map<String, Object> vehicleMissingMandatory() {
        return Map.of(
            "make", "UMM"
        );
    }
}
