package pt.ridenexus.vehicle.unit.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.services.VehicleRepository;
import pt.ridenexus.vehicle.services.VehicleService;
import pt.ridenexus.vehicle.services.exception.VehicleExistsException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class VehicleServiceTest {

    private final VehicleRepository mockRepo = Mockito.mock(VehicleRepository.class);
    private final VehicleService service = new VehicleService(mockRepo);

    @Test
    void testAddVehicleWhenAlreadyExistsThenExceptionThrown() {
        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Vehicle vehicle = Vehicle.builder()
            .countryCode(countryPT)
            .licensePlate(licensePlate)
            .build();

        Mockito.when(mockRepo.vehicleExists(countryPT, null, licensePlate))
            .thenReturn(true);

        Assertions.assertThrows(VehicleExistsException.class, () -> service.addVehicle(vehicle));

        Mockito.verify(mockRepo, Mockito.never()).addVehicle(any());
    }

    @Test
    void testAddVehicleWhenVehicleNotExistsThenPersist() {
        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Vehicle vehicle = Vehicle.builder()
            .countryCode(countryPT)
            .licensePlate(licensePlate)
            .build();

        Mockito.when(mockRepo.vehicleExists(countryPT, null, licensePlate))
            .thenReturn(false);
        Mockito.when(mockRepo.addVehicle(vehicle))
            .thenReturn(vehicle);

        Vehicle result = service.addVehicle(vehicle);

        Assertions.assertEquals(result.getLicensePlate(), vehicle.getLicensePlate());
        Mockito.verify(mockRepo, times(1)).addVehicle(vehicle);
    }
}