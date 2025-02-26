package pt.ridenexus.vehicle.services.vehicle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.ridenexus.vehicle.mapper.Service2PersistenceVehicleMapperImpl;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;
import pt.ridenexus.vehicle.persistence.rdb.VehicleRepository;
import pt.ridenexus.vehicle.services.exception.EntityExistsException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class VehicleServiceTest {

    private final VehicleRepository mockRepo = Mockito.mock(VehicleRepository.class);
    private final VehicleService service = new VehicleService(mockRepo, new Service2PersistenceVehicleMapperImpl());

    @Test
    void testAddVehicleWhenAlreadyExistsThenExceptionThrown() {
        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";
        Vehicle vehicle = Vehicle.builder()
            .countryCode(countryPT)
            .licensePlate(licensePlate)
            .build();

        Mockito.when(mockRepo.existsByCountryCodeAndRegionAndLicensePlate(countryPT, null, licensePlate))
            .thenReturn(true);

        Assertions.assertThrows(EntityExistsException.class, () -> service.addVehicle(vehicle));

        Mockito.verify(mockRepo, Mockito.never()).save(any());
    }

    @Test
    void testAddVehicleWhenVehicleNotExistsThenPersist() {
        String countryPT = "PT";
        String licensePlate = "AA-BB-CC";

        Vehicle vehicle = Vehicle.builder()
            .countryCode(countryPT)
            .licensePlate(licensePlate)
            .build();

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setCountryCode(countryPT);
        vehicleEntity.setLicensePlate(licensePlate);

        Mockito.when(mockRepo.existsByCountryCodeAndRegionAndLicensePlate(countryPT, null, licensePlate))
            .thenReturn(false);
        Mockito.when(mockRepo.save(any()))
            .thenReturn(vehicleEntity);

        Vehicle result = service.addVehicle(vehicle);

        Assertions.assertEquals(result.getLicensePlate(), vehicle.getLicensePlate());
        Mockito.verify(mockRepo, times(1)).save(any());
    }
}