package pt.ridenexus.vehicle.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ridenexus.vehicle.mapper.Service2WebTrailerMapper;
import pt.ridenexus.vehicle.services.trailer.Trailer;
import pt.ridenexus.vehicle.services.trailer.TrailerService;
import pt.ridenexus.vehicle.web.api.PageDto;
import pt.ridenexus.vehicle.web.api.TrailerDto;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.UpdateTrailer;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.AddTrailer;

@RestController
@RequestMapping("/api/v1/trailers")
@RequiredArgsConstructor
public class TrailerControllerV1 {

    private final TrailerService trailerService;
    private final Service2WebTrailerMapper mapper;

    @GetMapping
    public ResponseEntity<PageDto<TrailerDto>> getTrailers(
        @RequestParam(defaultValue = "0") Integer pageNumber,
        @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<TrailerDto> trailers = trailerService.getTrailers(pageNumber, pageSize).map(mapper::map);

        return new ResponseEntity<>(mapper.map(trailers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrailerDto> getTrailer(@PathVariable Long id) {

        Trailer trailer = trailerService.getTrailer(id);

        return new ResponseEntity<>(mapper.map(trailer), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrailerDto> addTrailer(@Validated(AddTrailer.class) @RequestBody TrailerDto request) {

        Trailer trailer = trailerService.addTrailer(mapper.map(request));

        return new ResponseEntity<>(mapper.map(trailer), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrailerDto> updateTrailer(
        @PathVariable Long id,
        @Validated(UpdateTrailer.class) @RequestBody TrailerDto request) {

        Trailer trailer = trailerService.updateTrailer(id, mapper.map(request));

        return new ResponseEntity<>(mapper.map(trailer), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTrailer(@PathVariable Long id) {

        trailerService.removeTrailer(id);

        return ResponseEntity.ok().build();
    }
}
