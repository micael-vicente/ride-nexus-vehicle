package pt.ridenexus.vehicle.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ridenexus.vehicle.mapper.Service2WebVehicleMapper;
import pt.ridenexus.vehicle.services.Vehicle;
import pt.ridenexus.vehicle.services.VehicleService;
import pt.ridenexus.vehicle.web.api.PageDto;
import pt.ridenexus.vehicle.web.api.VehicleDto;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.AddVehicle;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.UpdateVehicle;

import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;
    private final Service2WebVehicleMapper mapper;

    @MutationMapping
    public VehicleDto addVehicle(@Validated(AddVehicle.class) @Argument VehicleDto vehicle) {
        Vehicle newVehicle = service.addVehicle(mapper.map(vehicle));

        return mapper.map(newVehicle);
    }

    @MutationMapping
    public Long removeVehicle(@Argument Long id) {
        return service.removeVehicle(id);
    }

    @MutationMapping
    public VehicleDto updateVehicle(@Argument Long id, @Validated(UpdateVehicle.class) @Argument VehicleDto vehicle) {
        Vehicle response = service.updateVehicle(id, mapper.map(vehicle));

        return mapper.map(response);
    }

    @QueryMapping
    public VehicleDto getVehicleById(@Argument Long id) {
        Vehicle vehicle = service.getVehicle(id);

        return mapper.map(vehicle);
    }

    @QueryMapping
    public PageDto<VehicleDto> getVehicles(@Argument Integer pageNumber, @Argument Integer pageSize) {
        Integer number = Optional.ofNullable(pageNumber).orElse(0);
        Integer size = Optional.ofNullable(pageSize).orElse(10);

        Page<VehicleDto> vehicles = service.getVehicles(number, size).map(mapper::map);

        return mapper.map(vehicles);
    }
}
