package com.mdotm.petapi.service;

import com.mdotm.petapi.dto.CreatePetRequest;
import com.mdotm.petapi.dto.PetResponse;
import com.mdotm.petapi.dto.UpdatePetRequest;
import com.mdotm.petapi.exception.PetNotFoundException;
import com.mdotm.petapi.mapper.PetMapper;
import com.mdotm.petapi.model.Pet;
import com.mdotm.petapi.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public PetResponse create(CreatePetRequest request) {
        Pet pet = PetMapper.toModel(request);
        Pet saved = petRepository.save(pet);
        return PetMapper.toResponse(saved);
    }

    @Override
    public PetResponse findById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
        return PetMapper.toResponse(pet);
    }

    @Override
    public List<PetResponse> findAll() {
        return PetMapper.toResponseList(petRepository.findAll());
    }

    @Override
    public PetResponse update(Long id, UpdatePetRequest request) {
        // Verify the pet exists — findById throws PetNotFoundException if absent.
        // This also means we only call save() when we know the record is present,
        // avoiding a phantom insert at a caller-supplied id.
        petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        Pet updated = PetMapper.toModel(request, id);
        Pet saved = petRepository.save(updated);
        return PetMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        boolean deleted = petRepository.deleteById(id);
        if (!deleted) {
            throw new PetNotFoundException(id);
        }
    }
}
