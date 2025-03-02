package pt.ridenexus.vehicle.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import pt.ridenexus.vehicle.mapper.Service2WebVehicleMapper;
import pt.ridenexus.vehicle.services.vehicle.Vehicle;
import pt.ridenexus.vehicle.services.vehicle.VehicleService;
import pt.ridenexus.vehicle.web.api.PageDto;
import pt.ridenexus.vehicle.web.api.VehicleDto;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.UpdateVehicle;
import pt.ridenexus.vehicle.web.validation.ValidationGroups.AddVehicle;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleControllerV1 {

    private final VehicleService vehicleService;
    private final Service2WebVehicleMapper mapper;

    @GetMapping
    public ResponseEntity<PageDto<VehicleDto>> getAllVehicles(
        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        Page<VehicleDto> vehicles = vehicleService.getVehicles(pageNumber, pageSize)
            .map(mapper::map);

        return ResponseEntity.ok(mapper.map(vehicles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id) {

        Vehicle vehicle = vehicleService.getVehicle(id);

        return ResponseEntity.ok(mapper.map(vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeVehicle(@PathVariable Long id) {

        vehicleService.removeVehicle(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@Validated(AddVehicle.class) @RequestBody VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleService.addVehicle(mapper.map(vehicleDto));

        return ResponseEntity.ok(mapper.map(vehicle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> updateVehicle(
        @PathVariable Long id,
        @Validated(UpdateVehicle.class) @RequestBody VehicleDto vehicleDto) {

        Vehicle vehicle = vehicleService.updateVehicle(id, mapper.map(vehicleDto));

        return ResponseEntity.ok(mapper.map(vehicle));
    }
}
