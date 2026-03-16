package com.mdotm.petapi.service;

import com.mdotm.petapi.dto.CreatePetRequest;
import com.mdotm.petapi.dto.PetResponse;
import com.mdotm.petapi.dto.UpdatePetRequest;

import java.util.List;

/**
 * Business logic contract for pet management.
 */
public interface PetService {

    PetResponse create(CreatePetRequest request);

    PetResponse findById(Long id);

    List<PetResponse> findAll();

    PetResponse update(Long id, UpdatePetRequest request);

    void delete(Long id);
}
