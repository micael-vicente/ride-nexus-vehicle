package pt.ridenexus.vehicle.web.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pt.ridenexus.vehicle.fixtures.VehiclesFixture;
import pt.ridenexus.vehicle.mapper.Service2WebVehicleMapperImpl;
import pt.ridenexus.vehicle.services.exception.ObjectNotFoundException;
import pt.ridenexus.vehicle.services.vehicle.Vehicle;
import pt.ridenexus.vehicle.services.vehicle.VehicleService;
import pt.ridenexus.vehicle.web.api.VehicleDto;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleControllerV1.class)
@Import(Service2WebVehicleMapperImpl.class)
class VehicleControllerV1Test {

    @MockitoBean
    private VehicleService vehicleService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllVehicles() throws Exception {
        //arrange
        Vehicle vehicle1 = VehiclesFixture.vehicle(1L, "PT", "AA-AA-AA");
        Vehicle vehicle2 = VehiclesFixture.vehicle(2L, "PT", "AB-AB-AB");
        List<Vehicle> vehicles = Arrays.asList(vehicle1, vehicle2);
        Page<Vehicle> page = new PageImpl<>(vehicles);
        when(vehicleService.getVehicles(0, 10)).thenReturn(page);

        //act & assert
        mockMvc.perform(get("/api/v1/vehicles").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].id").value(1L))
            .andExpect(jsonPath("$.content[1].id").value(2L))
            .andExpect(jsonPath("$.content[0].licensePlate").value("AA-AA-AA"))
            .andExpect(jsonPath("$.content[1].licensePlate").value("AB-AB-AB"));
        verify(vehicleService).getVehicles(0, 10);
    }

    @Test
    void getVehicleById() throws Exception {
        //arrange
        Long id = 1L;
        Vehicle vehicle1 = VehiclesFixture.vehicle(1L, "PT", "AA-AA-AA");
        when(vehicleService.getVehicle(id)).thenReturn(vehicle1);

        //act & assert
        mockMvc.perform(get("/api/v1/vehicles/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.licensePlate").value("AA-AA-AA"))
            .andExpect(jsonPath("$.countryCode").value("PT"));
        verify(vehicleService).getVehicle(id);
    }

    @Test
    void getVehicleById_vehicleNotFound() throws Exception {
        //arrange
        Long id = 1L;
        when(vehicleService.getVehicle(id)).thenThrow(new ObjectNotFoundException("vehicle not found"));

        //act & assert
        mockMvc.perform(get("/api/v1/vehicles/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        verify(vehicleService).getVehicle(id);
    }

    @Test
    void removeVehicleById() throws Exception {
        //arrange
        Long id = 1L;
        when(vehicleService.removeVehicle(id)).thenReturn(id);

        //act & assert
        mockMvc.perform(delete("/api/v1/vehicles/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(vehicleService).removeVehicle(id);
    }

    @Test
    void addVehicle() throws Exception {
        //arrange
        Long id = 1L;
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        Vehicle addedVehicle = VehiclesFixture.vehicle(1L, "PT", "AA-AA-AA");
        when(vehicleService.addVehicle(any(Vehicle.class))).thenReturn(addedVehicle);

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.licensePlate").value("AA-AA-AA"))
            .andExpect(jsonPath("$.countryCode").value("PT"));
        verify(vehicleService).addVehicle(any(Vehicle.class));
    }

    @Test
    void addVehicle_countryDoesNotExist() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "AA", "AA-AA-AA");

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest());
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_statusNotValid() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        requestBody.setStatus("invalid");
        requestBody.setVehicleType("LIGHT_VEHICLE");

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.status")
                .value("The value provided is not one of: AVAILABLE, BUSY"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_vehicleTypeNotValid() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("invalid");

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.vehicleType")
                .value("The value provided is not one of: LIGHT_VEHICLE, HEAVY_VEHICLE, MOTORCYCLE"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_countryIsNull() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, null, "AA-AA-AA");
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("LIGHT_VEHICLE");

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.countryCode").value("must not be blank"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_licensePlateIsNull() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", null);
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("LIGHT_VEHICLE");

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.licensePlate").value("must not be blank"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_ownerIdIsNull() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("LIGHT_VEHICLE");
        requestBody.setOwnerId(null);

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.ownerId").value("must not be blank"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_mileageIsNull() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("LIGHT_VEHICLE");
        requestBody.setMileage(null);

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.mileage").value("must not be null"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_weightIsNull() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("LIGHT_VEHICLE");
        requestBody.setWeight(null);

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.weight").value("must not be null"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_mileageIsNegative() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("LIGHT_VEHICLE");
        requestBody.setMileage(-1);

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.mileage").value("must be greater than or equal to 0"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void addVehicle_weightIsNegative() throws Exception {
        //arrange
        VehicleDto requestBody = VehiclesFixture.vehicleDto(null, "PT", "AA-AA-AA");
        requestBody.setStatus("BUSY");
        requestBody.setVehicleType("LIGHT_VEHICLE");
        requestBody.setWeight(-1);

        //act & assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.errors.weight").value("must be greater than 0"));
        verifyNoInteractions(vehicleService);
    }

    @Test
    void editVehicle() throws Exception {
        //arrange
        Long id = 1L;
        VehicleDto requestBody = VehicleDto.builder()
            .status("AVAILABLE")
            .build();

        Vehicle editedVehicle = VehiclesFixture.vehicle(id, "PT", "AA-AA-AA");
        when(vehicleService.updateVehicle(eq(id), any(Vehicle.class))).thenReturn(editedVehicle);

        //act & assert
        mockMvc.perform(put("/api/v1/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
        verify(vehicleService).updateVehicle(eq(id), any(Vehicle.class));
    }

}
