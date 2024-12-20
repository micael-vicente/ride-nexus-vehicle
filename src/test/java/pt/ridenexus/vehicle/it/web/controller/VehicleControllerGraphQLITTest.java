package pt.ridenexus.vehicle.it.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import pt.ridenexus.vehicle.it.containers.BaseITTest;
import pt.ridenexus.vehicle.it.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.it.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.persistence.rdb.JpaVehicleRepository;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.services.VehicleRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

class VehicleControllerGraphQLITTest extends BaseITTest {

    @Autowired
    HttpGraphQlTester graphQlTester;

    @Autowired
    VehicleRepository repository;

    @Autowired
    JpaVehicleRepository myRepo;

    @BeforeEach
    void cleanUp() {
        myRepo.deleteAll();
    }

    @Test
    void testAddVehicleIsSuccessful() {

        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        graphQlTester.document(GraphQLDocuments.addVehicle())
                .variable("vehicle", vehicle)
                .executeAndVerify();

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

        Map<String, Object> vehicle = VehiclesFixture.vehicleMissingMandatory();

        graphQlTester.document(GraphQLDocuments.addVehicle())
                .variable("vehicle", vehicle)
                .execute()
                .errors()
                .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
                .verify();
    }

    @Test
    void testGetAllVehiclesShouldReturnAllVehicles() {
        final int expectedVehicles = 5;

        IntStream.range(0, expectedVehicles)
            .mapToObj(i -> VehiclesFixture.vehicle("PT", "AA-BB-0" + i))
            .forEach(vehicle ->
                    graphQlTester.document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).execute());

        List<Vehicle> vehicles = repository.getVehicles();

        Assertions.assertEquals(expectedVehicles, vehicles.size());

        graphQlTester.document(GraphQLDocuments.getAllVehicles(List.of("id")))
            .execute()
            .path("getVehicles.content[*].id")
            .entityList(Long.class)
            .hasSize(expectedVehicles);

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