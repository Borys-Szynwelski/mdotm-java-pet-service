# Pet API

This project is a simple REST API for managing pets, built with Spring Boot 3 and Java 21.

I prepared it as a home assignment for MDOTM. The goal was not to overengineer the solution, but to build something clean, readable, and easy to extend later.

---

## Project Overview

The application exposes a small set of CRUD endpoints for creating, reading, updating, and deleting pets.

While the task itself is intentionally simple, I approached it with the same mindset I would use in a real project: keep responsibilities separated, keep the code easy to follow, and avoid coupling the business logic to a specific persistence technology.

One important requirement in the assignment was to keep the code ready for a future switch from a relational database to a non-relational one. Because of that, I introduced a repository abstraction and kept the current persistence implementation in memory.

---

## Architecture

The project follows a straightforward layered structure:

- **Controller** handles HTTP requests and responses
- **Service** contains the application logic
- **Repository** defines the persistence contract
- **Repository implementation** provides the current in-memory storage
- **DTOs** are used for request and response payloads
- **Mapper** converts between DTOs and domain objects
- **Exception handling** is centralized so API errors stay consistent

### Package structure

```text
com.mdotm.petapi
├── PetApiApplication.java
├── controller/
│   └── PetController.java
├── service/
│   ├── PetService.java
│   └── PetServiceImpl.java
├── repository/
│   ├── PetRepository.java
│   └── InMemoryPetRepository.java
├── model/
│   └── Pet.java
├── dto/
│   ├── CreatePetRequest.java
│   ├── UpdatePetRequest.java
│   └── PetResponse.java
├── mapper/
│   └── PetMapper.java
└── exception/
    ├── PetNotFoundException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java