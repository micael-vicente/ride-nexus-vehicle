package pt.ridenexus.vehicle.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ridenexus.vehicle.mapper.Service2WebTrailerMapper;
import pt.ridenexus.vehicle.services.trailer.Trailer;
import pt.ridenexus.vehicle.services.trailer.TrailerService;
import pt.ridenexus.vehicle.web.api.PageDto;
import pt.ridenexus.vehicle.web.api.TrailerDto;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.AddTrailer;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.UpdateTrailer;

import java.util.Optional;

@RestController
@RequestMapping("/api/trailers")
@RequiredArgsConstructor
public class TrailerController {

    private final TrailerService service;
    private final Service2WebTrailerMapper mapper;

    @MutationMapping
    public TrailerDto addTrailer(@Validated(AddTrailer.class) @Argument TrailerDto trailer) {
        Trailer newTrailer = service.addTrailer(mapper.map(trailer));

        return mapper.map(newTrailer);
    }

    @MutationMapping
    public Long removeTrailer(@Argument Long id) {
        return service.removeTrailer(id);
    }

    @MutationMapping
    public TrailerDto updateTrailer(@Argument Long id, @Validated(UpdateTrailer.class) @Argument TrailerDto trailer) {
        Trailer response = service.updateTrailer(id, mapper.map(trailer));

        return mapper.map(response);
    }

    @QueryMapping
    public TrailerDto trailerById(@Argument Long id) {
        Trailer trailer = service.getTrailer(id);

        return mapper.map(trailer);
    }

    @QueryMapping
    public PageDto<TrailerDto> trailers(@Argument Integer pageNumber, @Argument Integer pageSize) {
        Integer number = Optional.ofNullable(pageNumber).orElse(0);
        Integer size = Optional.ofNullable(pageSize).orElse(10);

        Page<TrailerDto> vehicles = service.getTrailers(number, size).map(mapper::map);

        return mapper.map(vehicles);
    }

    @QueryMapping
    public PageDto<TrailerDto> trailersByOwnerId(@Argument String ownerId, @Argument Integer pageNumber, @Argument Integer pageSize) {
        Integer number = Optional.ofNullable(pageNumber).orElse(0);
        Integer size = Optional.ofNullable(pageSize).orElse(10);

        Page<TrailerDto> trailers = service.getTrailersByOwner(ownerId, number, size).map(mapper::map);

        return mapper.map(trailers);
    }
}
