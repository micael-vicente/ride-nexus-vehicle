package pt.ridenexus.vehicle.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.ridenexus.vehicle.containers.BaseIT;
import pt.ridenexus.vehicle.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class VehicleControllerUpdateIT extends BaseIT {
    @Test
    void testUpdateVehicleFailsIfVehicleDoesNotExist() {
        Set<String> expectedErrors = Set.of(
                "Vehicle not found. Id: 1"
        );

        Map<String, Object> vehicleUpdate = VehiclesFixture.vehicleMissingMandatory();

        getGraphQlTester().document(GraphQLDocuments.updateVehicle())
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

        getGraphQlTester().document(GraphQLDocuments.updateVehicle())
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

        getGraphQlTester().document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).execute();

        VehicleEntity vehicleEntity = getRepo().findAll().getFirst();

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

        getGraphQlTester().document(GraphQLDocuments.updateVehicle())
                .variable("id", id)
                .variable("vehicle", vehicleUpdate)
                .executeAndVerify();

        List<VehicleEntity> all = getRepo().findAll();

        Assertions.assertEquals(1, all.size());

        VehicleEntity updated = all.getFirst();

        Assertions.assertEquals(make, updated.getMake());
        Assertions.assertEquals(model, updated.getModel());
        Assertions.assertEquals(version, updated.getVersion());
        Assertions.assertEquals(vehicleEntity.getLicensePlate(), updated.getLicensePlate());
        Assertions.assertEquals(vehicleEntity.getLicensePlateDate(), updated.getLicensePlateDate());
        Assertions.assertEquals(vehicleEntity.getCountryCode(), updated.getCountryCode());
    }
}
