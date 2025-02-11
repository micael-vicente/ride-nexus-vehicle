package pt.ridenexus.vehicle.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.ridenexus.vehicle.containers.BaseIT;
import pt.ridenexus.vehicle.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

class VehicleControllerGetAllByOwnerIT extends BaseIT {

    @Test
    void testGetVehiclesByOwnerIdShouldReturnOnlyVehiclesWithThatOwner() {
        final int expectedVehicles = 5;
        final String ownerId = "owner1";

        //5 for owner1
        IntStream.range(0, expectedVehicles)
            .mapToObj(i -> VehiclesFixture.vehicle("PT", "AA-BB-0" + i))
            .map(v -> addOwner(ownerId, v))
            .forEach(v -> getGraphQlTester().document(GraphQLDocuments.addVehicle()).variable("vehicle", v).execute());

        //1 for null owner2
        Map<String, Object> vehicle = addOwner("owner2", VehiclesFixture.vehicle("PT", "SO-ME-77"));
        getGraphQlTester().document(GraphQLDocuments.addVehicle())
            .variable("vehicle", vehicle)
            .execute();

        List<VehicleEntity> vehicles = getRepo().findAll();

        Assertions.assertEquals(expectedVehicles + 1, vehicles.size());

        getGraphQlTester().document(GraphQLDocuments.getByOwnerId(List.of("id")))
            .variable("owner", ownerId)
            .execute()
            .path("vehiclesByOwnerId.content[*].id")
            .entityList(Long.class)
            .hasSize(expectedVehicles);
    }

    private Map<String, Object> addOwner(String ownerId, Map<String, Object> vehicle) {
        Map<String, Object> vehicleWithOwner = new HashMap<>(vehicle);
        vehicleWithOwner.put("ownerId", ownerId);
        return vehicleWithOwner;
    }
}
