package pt.ridenexus.vehicle.web.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pt.ridenexus.vehicle.fixtures.TrailersFixture;
import pt.ridenexus.vehicle.mapper.Service2WebTrailerMapperImpl;
import pt.ridenexus.vehicle.services.exception.EntityExistsException;
import pt.ridenexus.vehicle.services.exception.ObjectNotFoundException;
import pt.ridenexus.vehicle.services.trailer.Trailer;
import pt.ridenexus.vehicle.services.trailer.TrailerService;
import pt.ridenexus.vehicle.services.trailer.TrailerStatus;
import pt.ridenexus.vehicle.web.api.TrailerDto;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrailerControllerV1.class)
@Import(Service2WebTrailerMapperImpl.class)
public class TrailerControllerV1Test {

    @MockitoBean
    private TrailerService trailerService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTrailers() throws Exception {

        Trailer trailer = TrailersFixture.trailer(1L, "PT", "AA-AA-AA");
        Page<Trailer> trailerPage = new PageImpl<>(Collections.singletonList(trailer), PageRequest.of(0, 10), 1);

        when(trailerService.getTrailers(0, 10))
            .thenReturn(trailerPage);

        mockMvc.perform(get("/api/v1/trailers").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isNotEmpty())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].id").value(trailer.getId()))
            .andExpect(jsonPath("$.content[0].countryCode").value(trailer.getCountryCode()))
            .andExpect(jsonPath("$.content[0].licensePlate").value(trailer.getLicensePlate()))
            .andExpect(jsonPath("$.pageInfo.pageNumber").value(0))
            .andExpect(jsonPath("$.pageInfo.pageSize").value(10))
            .andExpect(jsonPath("$.pageInfo.totalElements").value(trailerPage.getTotalElements()));

        verify(trailerService).getTrailers(0, 10);
    }

    @Test
    void testGetTrailerById() throws Exception {
        Trailer trailer = TrailersFixture.trailer(1L, "PT", "AA-AA-AA");

        when(trailerService.getTrailer(1L)).thenReturn(trailer);

        mockMvc.perform(get("/api/v1/trailers/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(trailer.getId()))
            .andExpect(jsonPath("$.countryCode").value(trailer.getCountryCode()))
            .andExpect(jsonPath("$.licensePlate").value(trailer.getLicensePlate()));

        verify(trailerService).getTrailer(1L);
    }

    @Test
    void testGetTrailerById_NotFound() throws Exception {
        when(trailerService.getTrailer(1L)).thenThrow(new ObjectNotFoundException("Trailer not found"));

        mockMvc.perform(get("/api/v1/trailers/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Trailer not found"))
            .andExpect(jsonPath("$.title").value("Bad Request"));

        verify(trailerService).getTrailer(1L);
    }

    @Test
    void testAddTrailer() throws Exception {
        TrailerDto request = TrailersFixture.trailerDto(null, "PT", "AA-AA-AA");
        Trailer result = TrailersFixture.trailer(1L, "PT", "AA-AA-AA");

        when(trailerService.addTrailer(any(Trailer.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/trailers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(result.getId()))
            .andExpect(jsonPath("$.countryCode").value(result.getCountryCode()))
            .andExpect(jsonPath("$.licensePlate").value(result.getLicensePlate()));

        verify(trailerService).addTrailer(any(Trailer.class));
    }

    @Test
    void testAddTrailer_maxFreightWeightNegative() throws Exception {
        TrailerDto request = TrailersFixture.trailerDto(null, "PT", "AA-AA-AA");
        request.setMaximumFreightWeight(-1.0);

        mockMvc.perform(post("/api/v1/trailers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("request body is invalid"))
            .andExpect(jsonPath("$.errors.maximumFreightWeight").value("must be greater than 0"));

        verifyNoInteractions(trailerService);
    }

    @Test
    void testAddTrailer_alreadyExists() throws Exception {
        TrailerDto request = TrailersFixture.trailerDto(null, "PT", "AA-AA-AA");

        when(trailerService.addTrailer(any(Trailer.class))).thenThrow(new EntityExistsException("Trailer already exists"));

        mockMvc.perform(post("/api/v1/trailers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Trailer already exists"));

        verify(trailerService).addTrailer(any(Trailer.class));
    }

    @Test
    void testUpdateTrailer() throws Exception {
        TrailerDto request = new TrailerDto();
        request.setStatus("FREE");
        Trailer trailer = TrailersFixture.trailer(1L, "PT", "AA-AA-AA");
        trailer.setStatus(TrailerStatus.FREE);

        when(trailerService.updateTrailer(eq(1L), any(Trailer.class))).thenReturn(trailer);

        mockMvc.perform(put("/api/v1/trailers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("FREE"));

        verify(trailerService).updateTrailer(eq(1L), any(Trailer.class));
    }

    @Test
    void testDeleteTrailer() throws Exception {
        doNothing().when(trailerService).removeTrailer(1L);

        mockMvc.perform(delete("/api/v1/trailers/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(trailerService).removeTrailer(1L);
    }


}
