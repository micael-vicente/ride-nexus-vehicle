package pt.ridenexus.vehicle.services.vehicle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pt.ridenexus.vehicle.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.mapper.Service2PersistenceVehicleMapperImpl;
import pt.ridenexus.vehicle.persistence.model.VehicleEntity;
import pt.ridenexus.vehicle.persistence.rdb.VehicleRepository;
import pt.ridenexus.vehicle.services.exception.EntityExistsException;
import pt.ridenexus.vehicle.services.exception.ObjectNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pt.ridenexus.vehicle.services.vehicle.VehicleStatus.AVAILABLE;

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


    @Test
    void getVehicle() {
        //arrange
        VehicleEntity existingVehicle = VehiclesFixture.vehicleEntity(1L, "PT", "AA-AA-AA");
        when(mockRepo.findById(1L)).thenReturn(Optional.of(existingVehicle));

        //act
        Vehicle vehicle = service.getVehicle(1L);

        //assert
        assertNotNull(vehicle);
        assertEquals("PT", vehicle.getCountryCode());
        assertEquals("AA-AA-AA", vehicle.getLicensePlate());
        verify(mockRepo).findById(1L);
    }

    @Test
    void getVehicle_NotFound() {
        //arrange
        when(mockRepo.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(ObjectNotFoundException.class, () -> service.getVehicle(1L));
    }

    @Test
    void getVehicles() {
        //arrange
        VehicleEntity existingVehicle = VehiclesFixture.vehicleEntity(1L, "PT", "AA-AA-AA");
        PageImpl<VehicleEntity> existingVehicles = new PageImpl<>(Collections.singletonList(existingVehicle));
        when(mockRepo.findAll(PageRequest.of(0, 10))).thenReturn(existingVehicles);

        //act
        Page<Vehicle> vehicles = service.getVehicles(0, 10);

        //assert
        assertNotNull(vehicles);
        assertEquals(1, vehicles.getTotalElements());
        Vehicle first = vehicles.getContent().getFirst();
        assertNotNull(first);

        assertEquals("PT", first.getCountryCode());
        assertEquals("AA-AA-AA", first.getLicensePlate());
        verify(mockRepo).findAll(PageRequest.of(0, 10));
    }

    @Test
    void removeVehicle() {
        //arrange
        doNothing().when(mockRepo).deleteById(1L);

        //act
        service.removeVehicle(1L);

        //assert
        verify(mockRepo).deleteById(1L);
    }

    @Test
    void updateVehicle() {
        //arrange
        String country = "PT";
        String licensePlate = "AA-AA-AA";

        Vehicle update = Vehicle.builder()
            .status(AVAILABLE)
            .build();

        VehicleEntity vehicle = VehiclesFixture.vehicleEntity(1L, country, licensePlate);
        vehicle.setStatus("BUSY");

        when(mockRepo.findById(1L)).thenReturn(Optional.of(vehicle));
        when(mockRepo.save(any(VehicleEntity.class))).thenAnswer(returnsFirstArg());

        //act
        Vehicle updatedVehicle = service.updateVehicle(1L, update);

        //assert
        verify(mockRepo).findById(1L);
        assertNotNull(updatedVehicle);
        assertEquals(AVAILABLE, updatedVehicle.getStatus());
    }

    @Test
    void updateVehicle_notFound() {
        //arrange
        Vehicle update = Vehicle.builder().build();
        when(mockRepo.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(ObjectNotFoundException.class, () -> service.updateVehicle(1L, update));
    }


}