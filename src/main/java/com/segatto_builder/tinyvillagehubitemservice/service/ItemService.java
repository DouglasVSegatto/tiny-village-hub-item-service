package com.segatto_builder.tinyvillagehubitemservice.service;

import com.segatto_builder.tinyvillagehubitemservice.dto.request.CreateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.request.UpdateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.ItemResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.PaginationResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.mapper.ItemMapper;
import com.segatto_builder.tinyvillagehubitemservice.model.entity.Item;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService implements IService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final IStorageService storageService;

    @Value("${image.upload.max-size-mb}")
    private int maxImageSizeMb;

    @Value("${image.upload.allowed-types}")
    private List<String> allowedImageTypes;

    @Value("${image.upload.max-images-per-item}")
    private int maxImagesPerItem;

    @Override
    public void updateStatus(String itemId, Status newStatus, UUID ownerId, String userRole) {
        Item item = findByIdAndValidateOwnership(itemId, ownerId, userRole);
        Status oldStatus = item.getStatus();

        validateStatusTransition(oldStatus, newStatus);

        item.setStatus(newStatus);
        itemRepository.save(item);

        log.info("UPDATED_STATUS from {} to {} by userId: {}, role: {} (itemId: {})",
                oldStatus, newStatus, ownerId, userRole, item.getId());

        //TODO: Handle Completed status change for questioner or rating etc...
    }

    @Override
    public void create(CreateRequestDto dto, UUID ownerId) {
        Item item = itemMapper.toEntity(dto);
        item.setOwnerId(ownerId);
        itemRepository.save(item);
        log.info("CREATED_ITEM by userId: {} (itemId: {})", ownerId, item.getId());
    }


    @Override
    public void update(String itemId, UpdateRequestDto dto, UUID ownerId, String userRole) {
        Item item = findByIdAndValidateOwnership(itemId, ownerId, userRole);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setType(dto.getType());
        item.setAvailabilityType(dto.getAvailabilityType());
        item.setCondition(dto.getCondition());
        itemRepository.save(item);
        log.info("UPDATED_ITEM by userId: {}, role: {} (itemId: {})", ownerId, userRole, item.getId());
    }

    @Override
    public void delete(String itemId, UUID ownerId, String userRole) {
        updateStatus(itemId, Status.DELETED, ownerId, userRole);
        log.info("DELETED_ITEM(SOFT) by userId: {}, role: {} (itemId: {})", ownerId, userRole, itemId);
    }

    @Transactional
    @Override
    public ItemResponseDto findById(String itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found with ID: " + itemId));
        return itemMapper.toResponse(item);
    }

    @Override
    public List<ItemResponseDto> getActiveItems() {
        List<Item> items = itemRepository.findByStatus(Status.ACTIVE);
        return itemMapper.toResponseList(items);
    }

    @Override
    public List<ItemResponseDto> findByOwnerId(UUID ownerId) {
        List<Item> items = itemRepository.findByOwnerIdAndStatusNot(ownerId, Status.DELETED);
        return itemMapper.toResponseList(items);
    }

    @Override
    public List<ItemResponseDto> listByCity(String city) {
        List<Item> items = findByCity(city);
        return itemMapper.toResponseList(items);
    }

    @Override
    public List<ItemResponseDto> listByNeighborhood(String neighborhood) {
        List<Item> items = findByNeighborhood(neighborhood);
        return itemMapper.toResponseList(items);
    }

    @Override
    public List<ItemResponseDto> listByState(String state) {
        List<Item> items = findByState(state);
        return itemMapper.toResponseList(items);
    }

    @Override
    public List<ItemResponseDto> listByCountry(String country) {
        List<Item> items = findByCountry(country);
        return itemMapper.toResponseList(items);
    }

    @Override
    public PaginationResponseDto<ItemResponseDto> listByOwnerIdPaginated(UUID ownerId, int page, int size) {
        Page<Item> itemPage = findByOwnerIdPaginated(ownerId, page, size);
        PaginationResponseDto<ItemResponseDto> response = buildPaginationResponse(itemPage);
        response.setStatusCount(getStatusCountsByOwnerId(ownerId));
        return response;
    }

    @Override
    public PaginationResponseDto<ItemResponseDto> listActiveItemsPaginated(int page, int size) {
        Page<Item> itemPage = findActiveItemsPaginated(page, size);
        return buildPaginationResponse(itemPage);
    }

    @Override
    public PaginationResponseDto<ItemResponseDto> listByCityPaginated(String city, int page, int size) {
        Page<Item> itemPage = findByCityPaginated(city, page, size);
        return buildPaginationResponse(itemPage);
    }

    @Override
    public PaginationResponseDto<ItemResponseDto> listByNeighborhoodPaginated(String neighborhood, int page, int size) {
        Page<Item> itemPage = findByNeighborhoodPaginated(neighborhood, page, size);
        return buildPaginationResponse(itemPage);
    }

    @Override
    public PaginationResponseDto<ItemResponseDto> listByStatePaginated(String state, int page, int size) {
        Page<Item> itemPage = findByStatePaginated(state, page, size);
        return buildPaginationResponse(itemPage);
    }

    @Override
    public PaginationResponseDto<ItemResponseDto> listByCountryPaginated(String country, int page, int size) {
        Page<Item> itemPage = findByCountryPaginated(country, page, size);
        return buildPaginationResponse(itemPage);
    }

    @Override
    public void removeImage(String itemId, int index, UUID ownerId, String userRole) {
        Item item = findByIdAndValidateOwnership(itemId, ownerId, userRole);

        String urlToDelete = item.removeImageUrl(index);

        storageService.deleteFile(urlToDelete);

        itemRepository.save(item);

        log.info("DELETED_IMAGE by userId: {}, role: {} (itemId: {}) - index: {}", ownerId, userRole, itemId, index);
    }

    @Override
    public String addImage(String itemId, MultipartFile file, UUID ownerId, String userRole) {

        // File is not empty check
        if (file == null || file.isEmpty()) {
            throw new IllegalStateException("File can not be null or empty");
        }

        // File size check
        long maxSizeBytes = maxImageSizeMb * 1024L * 1024L;
        if (file.getSize() > maxSizeBytes) {
            throw new IllegalArgumentException("File size must not exceed " + maxImageSizeMb);
        }

        //File extension check
        String contentType = file.getContentType();
        if (contentType == null || !allowedImageTypes.contains(contentType)) {
            throw new IllegalArgumentException("Only " + String.join(", ", allowedImageTypes) + " are allowed");
        }

        Item item = findByIdAndValidateOwnership(itemId, ownerId, userRole);

        // Images per item check
        if (item.getImageUrls().size() >= maxImagesPerItem) {
            throw new IllegalStateException("Maximum " + maxImagesPerItem + " images allowed");
        }

        String imageUrl = storageService.uploadFile(file, item.getId());

        item.addImageUrl(imageUrl);
        itemRepository.save(item);

        log.info("ADDED_IMAGE by userId: {}, role: {} (itemId: {})", ownerId, userRole, itemId);
        return imageUrl;
    }

    @Override
    public List<String> getImages(String itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
        return new ArrayList<>(item.getImageUrls());
    }

    // Private helper methods


    private List<Item> findByNeighborhood(String neighborhood) {
        return itemRepository.findByStatusAndOwnerNeighbourhoodIgnoreCase(Status.ACTIVE, neighborhood);
    }

    private List<Item> findByCity(String city) {
        return itemRepository.findByStatusAndOwnerCityIgnoreCase(Status.ACTIVE, city);
    }

    private List<Item> findByState(String state) {
        return itemRepository.findByStatusAndOwnerStateIgnoreCase(Status.ACTIVE, state);
    }

    private List<Item> findByCountry(String country) {
        return itemRepository.findByStatusAndOwnerCountryIgnoreCase(Status.ACTIVE, country);
    }

    //Private Helper methods - Pagination
    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    }

    private PaginationResponseDto<ItemResponseDto> buildPaginationResponse(Page<Item> itemPage) {
        List<ItemResponseDto> items = itemMapper.toResponseList(itemPage.getContent());
        return new PaginationResponseDto<>(
                items,
                itemPage.getNumber(),
                itemPage.getSize(),
                itemPage.getTotalElements(),
                itemPage.getTotalPages(),
                itemPage.hasNext(),
                //Filled manually later
                null
        );
    }

    private Page<Item> findByOwnerIdPaginated(UUID ownerId, int page, int size) {
        return itemRepository.findPageByOwnerIdAndStatusNot(ownerId, Status.DELETED, createPageable(page, size));
    }

    private Page<Item> findActiveItemsPaginated(int page, int size) {
        return itemRepository.findPageByStatus(Status.ACTIVE, createPageable(page, size));
    }

    private Page<Item> findByCityPaginated(String city, int page, int size) {
        return itemRepository.findPageByStatusAndOwnerCityIgnoreCase(Status.ACTIVE, city, createPageable(page, size));
    }

    private Page<Item> findByNeighborhoodPaginated(String neighborhood, int page, int size) {
        return itemRepository.findPageByStatusAndOwnerNeighbourhoodIgnoreCase(Status.ACTIVE, neighborhood, createPageable(page, size));
    }

    private Page<Item> findByStatePaginated(String state, int page, int size) {
        return itemRepository.findPageByStatusAndOwnerStateIgnoreCase(Status.ACTIVE, state, createPageable(page, size));
    }

    private Page<Item> findByCountryPaginated(String country, int page, int size) {
        return itemRepository.findPageByStatusAndOwnerCountryIgnoreCase(Status.ACTIVE, country, createPageable(page, size));
    }

    private Item findByIdAndValidateOwnership(String id, UUID ownerId, String userRole) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));

        // Only owner or ADMIN can update
        if (!item.getOwnerId().equals(ownerId) && !"ADMIN".equals(userRole)) {
            throw new SecurityException("You don't have permission to update this item");
        }

        return item;
    }

    private Map<String, Long> getStatusCountsByOwnerId(UUID ownerId) {
        Map<String, Long> counts = new HashMap<>();
        for (Status status : Status.values()) {
            if (status != Status.DELETED) {
                Long count = itemRepository.countByOwnerIdAndStatus(ownerId, status);
                if (count > 0) {
                    counts.put(status.name(), count);
                }
            }
        }
        return counts;
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