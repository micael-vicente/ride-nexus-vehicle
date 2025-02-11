package pt.ridenexus.vehicle.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.ridenexus.vehicle.containers.BaseIT;
import pt.ridenexus.vehicle.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;

import java.util.List;
import java.util.stream.IntStream;

class VehicleControllerGetAllIT extends BaseIT {

    @Test
    void testGetAllVehiclesShouldReturnAllVehicles() {
        final int expectedVehicles = 5;

        IntStream.range(0, expectedVehicles)
                .mapToObj(i -> VehiclesFixture.vehicle("PT", "AA-BB-0" + i))
                .forEach(vehicle ->
                        getGraphQlTester().document(GraphQLDocuments.addVehicle()).variable("vehicle", vehicle).execute());

        List<VehicleEntity> vehicles = getRepo().findAll();

        Assertions.assertEquals(expectedVehicles, vehicles.size());

        getGraphQlTester().document(GraphQLDocuments.getAllVehicles(List.of("id")))
                .execute()
                .path("vehicles.content[*].id")
                .entityList(Long.class)
                .hasSize(expectedVehicles);
    }
}
