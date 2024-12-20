package pt.ridenexus.vehicle.it.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import pt.ridenexus.vehicle.it.containers.BaseIT;
import pt.ridenexus.vehicle.it.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.it.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;
import pt.ridenexus.vehicle.persistence.rdb.JpaVehicleRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

class VehicleControllerGraphQlIT extends BaseIT {

    @Autowired
    HttpGraphQlTester graphQlTester;

    @Autowired
    JpaVehicleRepository repo;

    @BeforeEach
    void cleanUp() {
        repo.deleteAll();
    }

    @Test
    void testAddVehicleIsSuccessful() {

        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        graphQlTester.document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .executeAndVerify();

        boolean vehicleExists = repo.existsByCountryCodeAndRegionAndLicensePlate(countryPT, null, licensePlate);

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
    void testAddVehicleFailsVehicleAlreadyExists() {
        Set<String> expectedErrors = Set.of(
            "Vehicle already exists"
        );

        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        graphQlTester.document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .executeAndVerify();

        graphQlTester.document(GraphQLDocuments.addVehicle())
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

        graphQlTester.document(GraphQLDocuments.addVehicle())
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

        graphQlTester.document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .execute()
            .errors()
            .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
            .verify();
    }

    @Test
    void testRemoveVehicleIfDoesNotExistThenDoNothing() {

        graphQlTester.document(GraphQLDocuments.removeVehicle())
            .variable("id", 1)
            .executeAndVerify();
    }

    @Test
    void testRemoveVehicleIfExistsThenRemove() {
        final int expectedVehicles = 5;

        IntStream.range(0, expectedVehicles)
            .mapToObj(i -> VehiclesFixture.vehicle("PT", "AA-BB-0" + i))
            .forEach(vehicle ->
                graphQlTester.document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).execute());

        VehicleEntity vehicle = repo.findAll().getFirst();

        Assertions.assertNotNull(vehicle);

        String licensePlate = vehicle.getLicensePlate();
        Long id = vehicle.getId();

        Assertions.assertNotNull(licensePlate);
        Assertions.assertNotNull(id);

        graphQlTester.document(GraphQLDocuments.removeVehicle())
            .variable("id", id)
            .executeAndVerify();

        boolean deleted = repo.findAll().stream().noneMatch(v -> licensePlate.equals(v.getLicensePlate()));

        Assertions.assertTrue(deleted);
    }

    @Test
    void testGetByIdThenSuccess() {
        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        graphQlTester.document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).executeAndVerify();

        List<VehicleEntity> all = repo.findAll();
        Assertions.assertEquals(1, all.size());

        graphQlTester.document(GraphQLDocuments.getById(List.of("licensePlate")))
            .variable("id", all.getFirst().getId())
            .execute()
            .path("getVehicleById.licensePlate")
            .entity(String.class)
            .isEqualTo(licensePlate);
    }

    @Test
    void testUpdateVehicleFailsIfVehicleDoesNotExist() {
        Set<String> expectedErrors = Set.of(
            "Vehicle not found. Id: 1"
        );

        Map<String, Object> vehicleUpdate = VehiclesFixture.vehicleMissingMandatory();

        graphQlTester.document(GraphQLDocuments.updateVehicle())
            .variable("id", 1L)
            .variable("vehicle", vehicleUpdate)
            .execute()
            .errors()
            .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
            .verify();
    }

    @Test
    void testUpdateVehicleFailsIfLicensePlateDateFormatNotValid() {
        Set<String> expectedErrors = Set.of(
            "licensePlateDate: Invalid date format, use: [yyyy-MM-dd]"
        );

        Map<String, Object> vehicleUpdate = Map.of(
            "licensePlateDate", "2020/01/01"
        );

        graphQlTester.document(GraphQLDocuments.updateVehicle())
            .variable("id", 1L)
            .variable("vehicle", vehicleUpdate)
            .execute()
            .errors()
            .expect(responseError -> containsAll(responseError.getMessage(), expectedErrors))
            .verify();
    }

    @Test
    void testUpdateVehicleOnlyProvidedFieldsUpdated() {
        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        graphQlTester.document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).execute();

        VehicleEntity vehicleEntity = repo.findAll().getFirst();

        Assertions.assertNotNull(vehicleEntity);

        Long id = vehicleEntity.getId();

        Assertions.assertNotNull(id);
        Assertions.assertNull(vehicleEntity.getMake());
        Assertions.assertNull(vehicleEntity.getModel());
        Assertions.assertNull(vehicleEntity.getVersion());

        String make = "UMM";
        String model = "XPTO";
        String version = "v10";
        Map<String, Object> vehicleUpdate = Map.of(
            "make", make,
            "model", model,
            "version", version
        );

        graphQlTester.document(GraphQLDocuments.updateVehicle())
            .variable("id", 1L)
            .variable("vehicle", vehicleUpdate)
            .executeAndVerify();

        List<VehicleEntity> all = repo.findAll();

        Assertions.assertEquals(1, all.size());

        VehicleEntity updated = all.getFirst();

        Assertions.assertEquals(make, updated.getMake());
        Assertions.assertEquals(model, updated.getModel());
        Assertions.assertEquals(version, updated.getVersion());
        Assertions.assertEquals(vehicleEntity.getLicensePlate(), updated.getLicensePlate());
        Assertions.assertEquals(vehicleEntity.getLicensePlateDate(), updated.getLicensePlateDate());
        Assertions.assertEquals(vehicleEntity.getCountryCode(), updated.getCountryCode());
    }

    @Test
    void testGetAllVehiclesShouldReturnAllVehicles() {
        final int expectedVehicles = 5;

        IntStream.range(0, expectedVehicles)
            .mapToObj(i -> VehiclesFixture.vehicle("PT", "AA-BB-0" + i))
            .forEach(vehicle ->
                graphQlTester.document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).execute());

        List<VehicleEntity> vehicles = repo.findAll();

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