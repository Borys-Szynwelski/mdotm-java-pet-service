package com.mdotm.petapi.dto;

/**
 * Response payload representing a pet.
 * Decouples the API contract from the internal domain model.
 */
public record PetResponse(
        Long id,
        String name,
        String species,
        Integer age,
        String ownerName
) {}
