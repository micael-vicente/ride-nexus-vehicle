package pt.ridenexus.vehicle.services.trailer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.ridenexus.vehicle.fixtures.TrailersFixture;
import pt.ridenexus.vehicle.mapper.Service2PersistenceTrailerMapperImpl;
import pt.ridenexus.vehicle.persistence.model.TrailerEntity;
import pt.ridenexus.vehicle.persistence.rdb.TrailerRepository;
import pt.ridenexus.vehicle.services.exception.EntityExistsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrailerServiceTest {

    private final TrailerRepository mockRepo = Mockito.mock(TrailerRepository.class);
    private final TrailerService service = new TrailerService(mockRepo, new Service2PersistenceTrailerMapperImpl());

    @Test
    void addTrailer() {
        //arrange
        String country = "PT";
        String licensePlate = "AA-AA-AA";
        Trailer trailer = TrailersFixture.trailer(null, country, licensePlate);
        TrailerEntity trailerEntity = TrailersFixture.trailerEntity(1L, country, licensePlate);

        when(mockRepo.existsByCountryCodeAndRegionAndLicensePlate(country, null, licensePlate)).thenReturn(false);
        when(mockRepo.save(any(TrailerEntity.class))).thenReturn(trailerEntity);

        //act
        Trailer addedTrailer = service.addTrailer(trailer);

        //assert
        assertNotNull(addedTrailer);
        assertEquals(country, addedTrailer.getCountryCode());
        assertEquals(licensePlate, addedTrailer.getLicensePlate());
        assertEquals(1L, addedTrailer.getId());

        verify(mockRepo).save(any(TrailerEntity.class));
        verify(mockRepo).existsByCountryCodeAndRegionAndLicensePlate(country, null, licensePlate);
    }

    @Test
    void addTrailer_alreadyExists() {
        //arrange
        String country = "PT";
        String licensePlate = "AA-AA-AA";
        Trailer trailer = TrailersFixture.trailer(null, country, licensePlate);

        when(mockRepo.existsByCountryCodeAndRegionAndLicensePlate(country, null, licensePlate)).thenReturn(true);

        //act & assert
        assertThrows(EntityExistsException.class, () -> service.addTrailer(trailer));

        verify(mockRepo).existsByCountryCodeAndRegionAndLicensePlate(country, null, licensePlate);
        verify(mockRepo, never()).save(any(TrailerEntity.class));
    }

    @Test
    void removeTrailer() {
        //arrange
        doNothing().when(mockRepo).deleteById(1L);

        //act
        service.removeTrailer(1L);

        //assert
        verify(mockRepo).deleteById(1L);
    }

    @Test
    void updateTrailer() {
        //arrange
        String country = "PT";
        String licensePlate = "AA-AA-AA";

        Trailer update = Trailer.builder()
            .status("AVAILABLE")
            .build();

        TrailerEntity trailer = TrailersFixture.trailerEntity(1L, country, licensePlate);
        trailer.setStatus("BUSY");

        when(mockRepo.findById(1L)).thenReturn(Optional.of(trailer));
        when(mockRepo.save(any(TrailerEntity.class))).thenAnswer(returnsFirstArg());

        //act
        Trailer updatedTrailer = service.updateTrailer(1L, update);

        //assert
        verify(mockRepo).findById(1L);
        assertNotNull(updatedTrailer);
        assertEquals("AVAILABLE", updatedTrailer.getStatus());
    }

    @Test
    void getTrailer() {
    }

    @Test
    void getTrailers() {
    }

    @Test
    void getTrailersByOwner() {
    }
}