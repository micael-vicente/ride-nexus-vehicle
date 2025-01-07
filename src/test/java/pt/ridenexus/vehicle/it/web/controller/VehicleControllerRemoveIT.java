package pt.ridenexus.vehicle.it.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import pt.ridenexus.vehicle.it.containers.BaseIT;
import pt.ridenexus.vehicle.it.fixtures.GraphQLDocuments;
import pt.ridenexus.vehicle.it.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;
import pt.ridenexus.vehicle.persistence.rdb.JpaVehicleRepository;

import java.util.stream.IntStream;

public class VehicleControllerRemoveIT extends BaseIT {

    @Autowired
    HttpGraphQlTester graphQlTester;

    @Autowired
    JpaVehicleRepository repo;

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

}
