package com.segatto_builder.tinyvillagehubitemservice.service;

import com.segatto_builder.tinyvillagehubitemservice.dto.request.CreateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.request.UpdateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.ItemResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IService {

    void create(CreateRequestDto dto, UUID ownerId);

    void update(String id, UpdateRequestDto dto, UUID ownerId, String userRole);

    void delete(String id, UUID ownerId, String userRole);

    void updateStatus(String id, Status newStatus, UUID ownerId, String userRole);

    ItemResponseDto findById(String itemId);

    List<ItemResponseDto> getActiveItems();

    List<ItemResponseDto> findByOwnerId(UUID ownerId);

    List<ItemResponseDto> listByCity(String city);

    List<ItemResponseDto> listByNeighborhood(String neighborhood);

    List<ItemResponseDto> listByState(String state);

    List<ItemResponseDto> listByCountry(String country);

    // Images
    String addImage(String itemId, MultipartFile file, UUID ownerId, String userRole);

    void removeImage(String itemId, int index, UUID ownerId, String userRole);

    List<String> getImages(String itemId);
}