package com.mdotm.petapi.model;

/**
 * Domain model representing a pet.
 * <p>
 * Intentionally kept as a plain Java class with no persistence annotations,
 * so the domain layer stays decoupled from any specific database technology.
 */
public class Pet {

    private Long id;
    private String name;
    private String species;
    private Integer age;
    private String ownerName;

    public Pet(Long id, String name, String species, Integer age, String ownerName) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
    }

    /** Copy constructor — used by the repository layer to avoid aliasing. */
    public Pet(Pet source) {
        this(source.id, source.name, source.species, source.age, source.ownerName);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public Integer getAge() { return age; }
    public String getOwnerName() { return ownerName; }
}
