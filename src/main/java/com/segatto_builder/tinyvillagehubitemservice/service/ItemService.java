package com.segatto_builder.tinyvillagehubitemservice.service;

import com.segatto_builder.tinyvillagehubitemservice.dto.request.RequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.ResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.mapper.Mapper;
import com.segatto_builder.tinyvillagehubitemservice.model.entity.Item;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.repository.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService implements IService {

    private final Repository repository;
    private final Mapper mapper;
    private final IStorageService storageService;

    @Override
    public void updateStatus(String itemId, Status newStatus, UUID ownerId) {
        Item item = findByIdAndValidateOwnership(itemId, ownerId);
        Status oldStatus = item.getStatus();

        validateStatusTransition(oldStatus, newStatus);

        item.setStatus(newStatus);
        repository.save(item);

        log.info("UPDATED_STATUS from {} to {} by userId: {} (itemId: {})",
                oldStatus, newStatus, ownerId, item.getId());

        //TODO: Handle Completed status change for questioner or rating etc...
    }

    @Override
    public void create(RequestDto dto, UUID ownerId) {
        Item item = mapper.toEntity(dto);
        item.setOwnerId(ownerId);
        repository.save(item);
        log.info("CREATED_ITEM by userId: {} (itemId: {})", ownerId, item.getId());
    }


    @Override
    public void update(String itemId, RequestDto dto, UUID ownerId) {
        Item item = findByIdAndValidateOwnership(itemId, ownerId);
        mapper.updateEntityFromDto(dto, item);
        repository.save(item);
        log.info("UPDATED_ITEM by userId: {} (itemId: {})", ownerId, item.getId());
    }

    @Override
    public void delete(String itemId, UUID ownerId) {
        updateStatus(itemId, Status.DELETED, ownerId);
        log.info("DELETED_ITEM(SOFT) by userId: {} (itemId: {})", ownerId, itemId);
    }

    @Transactional
    @Override
    public ResponseDto findById(String itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found with ID: " + itemId));
        return mapper.toResponse(item);
    }

    @Override
    public List<ResponseDto> findByOwnerId(UUID ownerId) {
        List<Item> items = repository.findByOwnerIdAndStatusNot(ownerId, Status.DELETED);
        return mapper.toResponseList(items);
    }

    @Override
    public List<ResponseDto> listByCity(String city) {
        List<Item> items = findByCity(city);
        return mapper.toResponseList(items);
    }

    @Override
    public List<ResponseDto> listByNeighborhood(String neighborhood) {
        List<Item> items = findByNeighborhood(neighborhood);
        return mapper.toResponseList(items);
    }

    @Override
    public List<ResponseDto> listByState(String state) {
        List<Item> items = findByState(state);
        return mapper.toResponseList(items);
    }

    @Override
    public List<ResponseDto> listByCountry(String country) {
        List<Item> items = findByCountry(country);
        return mapper.toResponseList(items);
    }

    @Override
    public void deleteImage(String itemId, int index, UUID ownerId) {
        Item item = findByIdAndValidateOwnership(itemId, ownerId);

        String urlToDelete = item.removeImageUrl(index); // Throws exception if invalid index

        storageService.deleteFile(urlToDelete);

        repository.save(item);

        log.info("DELETED_IMAGE by userId: {} (itemId: {}) - index: {}", ownerId, itemId, index);
    }

    @Override
    public String addImage(String itemId, MultipartFile file, UUID ownerId) {
        Item item = findByIdAndValidateOwnership(itemId, ownerId);

        if (item.getImageUrls().size() >= 5) {
            throw new IllegalStateException("Maximum 5 images allowed");
        }

        String imageUrl = storageService.uploadFile(file, item.getId());

        item.addImageUrl(imageUrl);
        repository.save(item);

        log.info("ADDED_IMAGE by userId: {} (itemId: {})", ownerId, itemId);
        return imageUrl;
    }

    @Override
    public List<String> getImages(String itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
        return new ArrayList<>(item.getImageUrls());
    }

    // Private helper methods
    private List<Item> findByNeighborhood(String neighborhood) {
        return repository.findByStatusAndOwnerNeighbourhoodIgnoreCase(Status.ACTIVE, neighborhood);
    }

    private List<Item> findByCity(String city) {
        return repository.findByStatusAndOwnerCityIgnoreCase(Status.ACTIVE, city);
    }

    private List<Item> findByState(String state) {
        return repository.findByStatusAndOwnerStateIgnoreCase(Status.ACTIVE, state);
    }

    private List<Item> findByCountry(String country) {
        return repository.findByStatusAndOwnerCountryIgnoreCase(Status.ACTIVE, country);
    }

    private Item findByIdAndValidateOwnership(String id, UUID ownerId) {
        return repository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new SecurityException("Item not found or user not authorized"));
    }

    private void validateStatusTransition(Status from, Status to) {
        // Status transitions rule
        Map<Status, Set<Status>> allowedTransitions = Map.of(
                Status.INACTIVE, Set.of(Status.ACTIVE, Status.DELETED),
                Status.ACTIVE, Set.of(Status.INACTIVE, Status.PENDING, Status.COMPLETED, Status.DELETED),
                Status.PENDING, Set.of(Status.ACTIVE, Status.INACTIVE, Status.COMPLETED, Status.DELETED),
                Status.COMPLETED, Set.of(Status.DELETED),
                Status.DELETED, Set.of(Status.INACTIVE) //TODO Review future options.
        );

        if (!allowedTransitions.get(from).contains(to)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s", from, to));
        }
    }
}