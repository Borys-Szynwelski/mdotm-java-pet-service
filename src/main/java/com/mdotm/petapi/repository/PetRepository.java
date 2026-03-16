package com.mdotm.petapi.repository;

import com.mdotm.petapi.model.Pet;

import java.util.List;
import java.util.Optional;

/**
 * Repository port (abstraction) for Pet persistence.
 * <p>
 * Defining persistence as an interface here means the service layer depends only on this
 * contract, not on any specific storage technology. Swapping the backing store
 * (e.g. replacing the in-memory map with a JPA or MongoDB implementation) requires
 * only a new class that implements this interface — no changes to the service or
 * controller layers are needed.
 */
public interface PetRepository {

    /**
     * Persists a pet. If the pet has no id, a new record is created and the
     * generated id is set on the returned instance. If an id is present the
     * existing record is replaced.
     *
     * @return a new instance reflecting the persisted state (including the generated id)
     */
    Pet save(Pet pet);

    Optional<Pet> findById(Long id);

    List<Pet> findAll();

    /**
     * Removes the pet with the given id.
     *
     * @return {@code true} if a pet was found and deleted, {@code false} if no
     *         pet with that id existed.
     */
    boolean deleteById(Long id);
}
