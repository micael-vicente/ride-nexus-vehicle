package pt.ridenexus.vehicle.services.trailer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pt.ridenexus.vehicle.mapper.Service2PersistenceTrailerMapper;
import pt.ridenexus.vehicle.persistence.model.TrailerEntity;
import pt.ridenexus.vehicle.persistence.rdb.TrailerRepository;
import pt.ridenexus.vehicle.services.exception.EntityExistsException;
import pt.ridenexus.vehicle.services.exception.ObjectNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrailerService {

    private final TrailerRepository repo;
    private final Service2PersistenceTrailerMapper mapper;

    public Trailer addTrailer(Trailer t) {
        boolean trailerExists = repo.existsByCountryCodeAndRegionAndLicensePlate(t.getCountryCode(), t.getRegion(), t.getLicensePlate());

        log.info("Persisting trailer with license plate: {}", t.getLicensePlate());
        if(trailerExists) {
            log.info("Trailer already exists for Country: {}, Region: {}, Plate: {}", t.getCountryCode(), t.getRegion(), t.getLicensePlate());
            throw new EntityExistsException("Trailer already exists");
        }

        TrailerEntity saved = repo.save(mapper.map(t));
        Trailer vehicle = mapper.map(saved);
        log.info("Trailer with license plate: {} has been persisted", t.getLicensePlate());

        return vehicle;
    }

    public void removeTrailer(Long id) {

        log.info("Removing trailer with id: {}", id);
        repo.deleteById(id);
        log.info("Trailer with id: {} has been removed", id);

    }

    public Trailer updateTrailer(Long id, Trailer t) {
        log.info("Updating trailer with id: {}", id);
        TrailerEntity trailer = repo.findById(id).orElse(null);

        if(trailer == null) {
            throw new ObjectNotFoundException("Trailer not found. Id: " + id);
        }

        mapper.update(t, trailer);
        TrailerEntity updated = repo.save(trailer);

        log.info("Trailer with id: {} has been updated", id);

        return mapper.map(updated);
    }

    public Trailer getTrailer(Long id) {
        log.info("Getting trailer with id: {}", id);

        TrailerEntity byId = repo.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Trailer not found. Id: " + id));

        return mapper.map(byId);
    }

    public Page<Trailer> getTrailers(Integer number, Integer size) {
        log.info("Getting all trailers");
        return repo.findAll(PageRequest.of(number, size))
            .map(mapper::map);
    }
}
