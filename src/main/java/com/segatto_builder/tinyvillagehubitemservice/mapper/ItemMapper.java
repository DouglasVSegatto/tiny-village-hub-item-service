package com.segatto_builder.tinyvillagehubitemservice.mapper;

import com.segatto_builder.tinyvillagehubitemservice.dto.request.CreateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.ItemResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.model.entity.Item;
import org.mapstruct.Mapping;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "imageUrls", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Item toEntity(CreateRequestDto request);

    ItemResponseDto toResponse(Item item);

    List<ItemResponseDto> toResponseList(List<Item> items);
}
