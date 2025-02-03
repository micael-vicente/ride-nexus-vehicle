package pt.ridenexus.vehicle.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.ridenexus.vehicle.containers.BaseIT;
import pt.ridenexus.vehicle.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.fixtures.VehiclesFixture;

import java.util.Map;
import java.util.Set;

class VehicleControllerAddIT extends BaseIT {

    @Test
    void testAddVehicleIsSuccessful() {

        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        getGraphQlTester().document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .executeAndVerify();

        boolean vehicleExists = getRepo().existsByCountryCodeAndRegionAndLicensePlate(countryPT, null, licensePlate);

        Assertions.assertTrue(vehicleExists);
    }

    @Test
    void testAddVehicleFailsMandatoryFieldsValidations() {
        Set<String> expectedErrors = Set.of(
          "weight: must not be null",
          "mileage: must not be null",
          "licensePlate: must not be blank",
          "licensePlateDate: must not be blank",
          "countryCode: must not be blank"
        );

        Map<String, Object> vehicle = VehiclesFixture.vehicleMissingMandatory();

        getGraphQlTester().document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .execute()
            .errors()
            .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
            .verify();
    }

    @Test
    void testAddVehicleFailsVehicleAlreadyExists() {
        Set<String> expectedErrors = Set.of(
            "Vehicle already exists"
        );

        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        getGraphQlTester().document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .executeAndVerify();

        getGraphQlTester().document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .execute()
            .errors()
            .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors));
    }

    @Test
    void testAddVehicleFailsLicensePlateDateFormatValidation() {
        Set<String> expectedErrors = Set.of(
            "licensePlateDate: Invalid date format, use: [yyyy-MM-dd]"
        );

        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = new java.util.HashMap<>(VehiclesFixture.vehicle(countryPT, licensePlate));
        vehicle.put("licensePlateDate", "01/01/2024");

        getGraphQlTester().document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .execute()
            .errors()
            .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
            .verify();
    }

    @Test
    void testAddVehicleFailsKnownCountriesValidation() {
        Set<String> expectedErrors = Set.of(
            "countryCode: Not a valid/registered country. Must be uppercase."
        );

        String countryPT = "PTO";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        getGraphQlTester().document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .execute()
            .errors()
            .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
            .verify();
    }


}