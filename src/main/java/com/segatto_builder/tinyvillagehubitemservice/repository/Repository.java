package com.segatto_builder.tinyvillagehubitemservice.repository;

import com.segatto_builder.tinyvillagehubitemservice.model.entity.Item;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface Repository extends MongoRepository<Item, String> {

    // Basic queries (same method names)
    Optional<Item> findByIdAndOwnerId(String id, UUID ownerId);

    List<Item> findByOwnerId(UUID ownerId);

    List<Item> findByOwnerIdAndStatusNot(UUID ownerId, Status status);

    List<Item> findByStatus(Status status);

    // Location-based queries (same method names work!)
    List<Item> findByStatusAndOwnerCityIgnoreCase(Status status, String city);

    List<Item> findByStatusAndOwnerNeighbourhoodIgnoreCase(Status status, String neighbourhood);

    List<Item> findByStatusAndOwnerStateIgnoreCase(Status status, String state);

    List<Item> findByStatusAndOwnerCountryIgnoreCase(Status status, String country);
}