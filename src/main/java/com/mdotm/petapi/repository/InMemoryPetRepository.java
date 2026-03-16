package com.mdotm.petapi.repository;

import com.mdotm.petapi.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of {@link PetRepository}.
 * <p>
 * Simulates a relational persistence layer using a {@link ConcurrentHashMap}
 * for thread-safe storage and an {@link AtomicLong} for id generation.
 * <p>
 * Defensive copies are made on every read and write so that callers cannot
 * accidentally mutate stored state through a shared reference — the same
 * isolation guarantee a real database provides.
 * <p>
 * This class is the only thing that would need to change when replacing the
 * mock persistence with a real database: provide a new {@code @Repository}
 * implementation (e.g. backed by Spring Data JPA or MongoDB) and remove or
 * deactivate this one.
 */
@Repository
public class InMemoryPetRepository implements PetRepository {

    private final Map<Long, Pet> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    /**
     * Stores a defensive copy of the pet. If no id is provided a new one is
     * generated. Returns a fresh copy reflecting the persisted state.
     */
    @Override
    public Pet save(Pet pet) {
        long id = pet.getId() != null ? pet.getId() : idSequence.getAndIncrement();
        Pet record = new Pet(id, pet.getName(), pet.getSpecies(), pet.getAge(), pet.getOwnerName());
        store.put(id, record);
        return new Pet(record);
    }

    /** Returns a defensive copy so callers cannot mutate stored state. */
    @Override
    public Optional<Pet> findById(Long id) {
        return Optional.ofNullable(store.get(id)).map(Pet::new);
    }

    /** Returns copies of all stored pets. */
    @Override
    public List<Pet> findAll() {
        return store.values().stream()
                .map(Pet::new)
                .toList();
    }

    @Override
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }
}
