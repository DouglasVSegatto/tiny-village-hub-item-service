package com.segatto_builder.tinyvillagehubitemservice.service;

import com.segatto_builder.tinyvillagehubitemservice.dto.request.RequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.ResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.model.entity.Item;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IService {

    Optional<Item> getItemById(String id);

    Optional<Item> getItemByIdAndOwner(String id, UUID ownerId);

    List<Item> getItemsByOwner(UUID ownerId);

    List<Item> getItemsByStatus(Status status);

    List<Item> getActiveItems();

    void updateStatus(String id, Status newStatus, UUID ownerId);

    void create(RequestDto dto, UUID ownerId);

    void update(String id, RequestDto dto, UUID ownerId);

    void delete(String id, UUID ownerId);

    ResponseDto findById(String itemId);

    List<ResponseDto> findAllByOwnerId(UUID ownerId);

    List<ResponseDto> listByCity(String city);

    List<ResponseDto> listByNeighborhood(String neighborhood);

    List<ResponseDto> listByState(String state);

    List<ResponseDto> listByCountry(String country);

    // Images
    String addImage(String itemId, MultipartFile file, UUID ownerId);
    void deleteImage(String itemId, int index, UUID ownerId);
    List<String> getImages(String itemId);
}