package pt.ridenexus.vehicle.it.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import pt.ridenexus.vehicle.it.containers.BaseITTest;
import pt.ridenexus.vehicle.it.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.services.VehicleRepository;

import java.util.Map;
import java.util.Set;

class VehicleControllerGraphQLITTest extends BaseITTest {

    @Autowired
    HttpGraphQlTester graphQlTester;

    @Autowired
    VehicleRepository repository;

    @Test
    void testAddVehicleIsSuccessful() {

        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = Map.of(
            "countryCode", countryPT,
            "licensePlate", licensePlate,
            "licensePlateDate", "2024-12-19",
            "weight", 1000,
            "mileage", 1
        );

        graphQlTester.document(GraphQLDocuments.addVehicle())
                .variable("vehicle", vehicle)
                .execute()
                .errors()
                .verify();

        boolean vehicleExists = repository.vehicleExists(countryPT, null, licensePlate);

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

        Map<String, Object> vehicle = Map.of(
                "make", "make"
        );

        graphQlTester.document(GraphQLDocuments.addVehicle())
                .variable("vehicle", vehicle)
                .execute()
                .errors()
                .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
                .verify();
    }

    private boolean containsAll(String toTest, Set<String> tokens) {
        for(String token : tokens) {
            if(!toTest.contains(token)) {
                return false;
            }
        }
        return true;
    }

}