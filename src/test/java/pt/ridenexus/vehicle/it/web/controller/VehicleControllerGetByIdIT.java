package pt.ridenexus.vehicle.it.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.ridenexus.vehicle.it.containers.BaseIT;
import pt.ridenexus.vehicle.it.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.it.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;

import java.util.List;
import java.util.Map;

public class VehicleControllerGetByIdIT extends BaseIT {


    @Test
    void testGetByIdThenSuccess() {
        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Map<String, Object> vehicle = VehiclesFixture.vehicle(countryPT, licensePlate);

        getGraphQlTester().document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).executeAndVerify();

        List<VehicleEntity> all = getRepo().findAll();
        Assertions.assertEquals(1, all.size());

        getGraphQlTester().document(GraphQLDocuments.getById(List.of("licensePlate")))
                .variable("id", all.getFirst().getId())
                .execute()
                .path("vehicleById.licensePlate")
                .entity(String.class)
                .isEqualTo(licensePlate);
    }
}
