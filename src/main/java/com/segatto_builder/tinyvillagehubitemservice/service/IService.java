package com.segatto_builder.tinyvillagehubitemservice.service;

import com.segatto_builder.tinyvillagehubitemservice.dto.request.CreateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.request.UpdateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.ResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IService {

    void create(CreateRequestDto dto, UUID ownerId);

    void update(String id, UpdateRequestDto dto, UUID ownerId, String userRole);

    void delete(String id, UUID ownerId, String userRole);

    void updateStatus(String id, Status newStatus, UUID ownerId, String userRole);

    ResponseDto findById(String itemId);

    List<ResponseDto> getActiveItems();

    List<ResponseDto> findByOwnerId(UUID ownerId);

    List<ResponseDto> listByCity(String city);

    List<ResponseDto> listByNeighborhood(String neighborhood);

    List<ResponseDto> listByState(String state);

    List<ResponseDto> listByCountry(String country);

    // Images
    String addImage(String itemId, MultipartFile file, UUID ownerId, String userRole);

    void deleteImage(String itemId, int index, UUID ownerId, String userRole);

    List<String> getImages(String itemId);
}