package com.mdotm.petapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdotm.petapi.dto.CreatePetRequest;
import com.mdotm.petapi.dto.PetResponse;
import com.mdotm.petapi.dto.UpdatePetRequest;
import com.mdotm.petapi.exception.PetNotFoundException;
import com.mdotm.petapi.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @RestControllerAdvice beans are part of the MVC slice and are loaded automatically
// by @WebMvcTest — no explicit @Import needed.
@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PetService petService;

    private static final PetResponse BUDDY = new PetResponse(1L, "Buddy", "Dog", 3, "Alice");

    // --- POST /api/pets ---

    @Test
    void createPet_shouldReturn201_whenRequestIsValid() throws Exception {
        when(petService.create(any(CreatePetRequest.class))).thenReturn(BUDDY);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreatePetRequest("Buddy", "Dog", 3, "Alice"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.species").value("Dog"));
    }

    @Test
    void createPet_shouldReturn400_whenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreatePetRequest("", "Dog", 3, null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void createPet_shouldReturn400_whenSpeciesIsBlank() throws Exception {
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreatePetRequest("Buddy", "", 3, null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("species"));
    }

    @Test
    void createPet_shouldReturn400_whenAgeIsNegative() throws Exception {
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreatePetRequest("Buddy", "Dog", -1, null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("age"));
    }

    @Test
    void createPet_shouldReturn400_whenBodyIsMalformedJson() throws Exception {
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not: valid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // --- GET /api/pets ---

    @Test
    void getAllPets_shouldReturn200WithList() throws Exception {
        when(petService.findAll()).thenReturn(List.of(BUDDY));

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Buddy"));
    }

    @Test
    void getAllPets_shouldReturn200WithEmptyList_whenNoPetsExist() throws Exception {
        when(petService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- GET /api/pets/{id} ---

    @Test
    void getPetById_shouldReturn200_whenFound() throws Exception {
        when(petService.findById(1L)).thenReturn(BUDDY);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void getPetById_shouldReturn404_whenNotFound() throws Exception {
        when(petService.findById(99L)).thenThrow(new PetNotFoundException(99L));

        mockMvc.perform(get("/api/pets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Pet not found with id: 99"));
    }

    // --- PUT /api/pets/{id} ---

    @Test
    void updatePet_shouldReturn200_whenFound() throws Exception {
        PetResponse updated = new PetResponse(1L, "Max", "Cat", 5, "Bob");
        when(petService.update(eq(1L), any(UpdatePetRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdatePetRequest("Max", "Cat", 5, "Bob"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Max"));
    }

    @Test
    void updatePet_shouldReturn404_whenNotFound() throws Exception {
        when(petService.update(eq(99L), any(UpdatePetRequest.class)))
                .thenThrow(new PetNotFoundException(99L));

        mockMvc.perform(put("/api/pets/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdatePetRequest("Max", "Cat", 5, "Bob"))))
                .andExpect(status().isNotFound());
    }

    // --- DELETE /api/pets/{id} ---

    @Test
    void deletePet_shouldReturn204_whenFound() throws Exception {
        doNothing().when(petService).delete(1L);

        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePet_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new PetNotFoundException(99L)).when(petService).delete(99L);

        mockMvc.perform(delete("/api/pets/99"))
                .andExpect(status().isNotFound());
    }
}
