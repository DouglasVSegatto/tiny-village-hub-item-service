package com.segatto_builder.tinyvillagehubitemservice.repository;

import com.segatto_builder.tinyvillagehubitemservice.model.entity.Item;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    // Basic queries
    List<Item> findByOwnerIdAndStatusNot(UUID ownerId, Status status);

    List<Item> findByStatus(Status status);

    // Location-based queries (List versions - existing)
    List<Item> findByStatusAndOwnerCityIgnoreCase(Status status, String city);

    List<Item> findByStatusAndOwnerNeighbourhoodIgnoreCase(Status status, String neighbourhood);

    List<Item> findByStatusAndOwnerStateIgnoreCase(Status status, String state);

    List<Item> findByStatusAndOwnerCountryIgnoreCase(Status status, String country);

    // Paginated queries (new - separate methods)
    Page<Item> findPageByOwnerIdAndStatusNot(UUID ownerId, Status status, Pageable pageable);

    Page<Item> findPageByStatus(Status status, Pageable pageable);

    Page<Item> findPageByStatusAndOwnerCityIgnoreCase(Status status, String city, Pageable pageable);

    Page<Item> findPageByStatusAndOwnerNeighbourhoodIgnoreCase(Status status, String neighbourhood, Pageable pageable);

    Page<Item> findPageByStatusAndOwnerStateIgnoreCase(Status status, String state, Pageable pageable);

    Page<Item> findPageByStatusAndOwnerCountryIgnoreCase(Status status, String country, Pageable pageable);
}