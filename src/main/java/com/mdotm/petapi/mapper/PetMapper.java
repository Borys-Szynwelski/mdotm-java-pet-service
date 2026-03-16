package com.mdotm.petapi.mapper;

import com.mdotm.petapi.dto.CreatePetRequest;
import com.mdotm.petapi.dto.PetResponse;
import com.mdotm.petapi.dto.UpdatePetRequest;
import com.mdotm.petapi.model.Pet;

import java.util.List;

/**
 * Stateless mapper between Pet domain objects and DTOs.
 * <p>
 * Kept as a utility class with static methods to avoid unnecessary Spring bean
 * overhead for pure data-transformation logic at this scale.
 */
public final class PetMapper {

    private PetMapper() {}

    public static Pet toModel(CreatePetRequest request) {
        return new Pet(null, request.name(), request.species(), request.age(), request.ownerName());
    }

    public static Pet toModel(UpdatePetRequest request, Long id) {
        return new Pet(id, request.name(), request.species(), request.age(), request.ownerName());
    }

    public static PetResponse toResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getAge(),
                pet.getOwnerName()
        );
    }

    public static List<PetResponse> toResponseList(List<Pet> pets) {
        return pets.stream()
                .map(PetMapper::toResponse)
                .toList();
    }
}
