package com.mdotm.petapi.service;

import com.mdotm.petapi.dto.CreatePetRequest;
import com.mdotm.petapi.dto.PetResponse;
import com.mdotm.petapi.dto.UpdatePetRequest;
import com.mdotm.petapi.exception.PetNotFoundException;
import com.mdotm.petapi.model.Pet;
import com.mdotm.petapi.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    private Pet savedPet;

    @BeforeEach
    void setUp() {
        savedPet = new Pet(1L, "Buddy", "Dog", 3, "Alice");
    }

    @Test
    void create_shouldReturnSavedPetResponse() {
        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        PetResponse response = petService.create(new CreatePetRequest("Buddy", "Dog", 3, "Alice"));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Buddy");
        assertThat(response.species()).isEqualTo("Dog");
        assertThat(response.age()).isEqualTo(3);
        assertThat(response.ownerName()).isEqualTo("Alice");
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void findById_shouldReturnPetResponse_whenPetExists() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(savedPet));

        PetResponse response = petService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Buddy");
    }

    @Test
    void findById_shouldThrowPetNotFoundException_whenPetDoesNotExist() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.findById(99L))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_shouldReturnListOfPetResponses() {
        when(petRepository.findAll()).thenReturn(List.of(savedPet));

        List<PetResponse> responses = petService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("Buddy");
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoPetsExist() {
        when(petRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(petService.findAll()).isEmpty();
    }

    @Test
    void update_shouldReturnUpdatedPetResponse_whenPetExists() {
        Pet updatedPet = new Pet(1L, "Max", "Cat", 2, "Bob");
        when(petRepository.findById(1L)).thenReturn(Optional.of(savedPet));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedPet);

        PetResponse response = petService.update(1L, new UpdatePetRequest("Max", "Cat", 2, "Bob"));

        assertThat(response.name()).isEqualTo("Max");
        assertThat(response.species()).isEqualTo("Cat");
    }

    @Test
    void update_shouldThrowPetNotFoundException_whenPetDoesNotExist() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.update(99L, new UpdatePetRequest("Max", "Cat", 2, "Bob")))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("99");

        verify(petRepository, never()).save(any());
    }

    @Test
    void delete_shouldSucceed_whenPetExists() {
        when(petRepository.deleteById(1L)).thenReturn(true);

        assertThatCode(() -> petService.delete(1L)).doesNotThrowAnyException();
        verify(petRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowPetNotFoundException_whenPetDoesNotExist() {
        when(petRepository.deleteById(99L)).thenReturn(false);

        assertThatThrownBy(() -> petService.delete(99L))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("99");
    }
}
