package com.segatto_builder.tinyvillagehubitemservice.controller;

import com.segatto_builder.tinyvillagehubitemservice.dto.request.CreateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.request.UpdateRequestDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.ItemResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.dto.response.PaginationResponseDto;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.service.IService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final IService itemService;

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody CreateRequestDto request,
            @RequestHeader("X-User-Id") UUID userId) {
        itemService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateRequestDto request,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String userRole) {
        itemService.update(id, request, userId, userRole);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String userRole) {
        itemService.delete(id, userId, userRole);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable String id,
            @RequestParam Status status,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String userRole) {
        itemService.updateStatus(id, status, userId, userRole);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable String id) {
        ItemResponseDto item = itemService.findById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getActiveItems() {
        List<ItemResponseDto> items = itemService.getActiveItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/my-items")
    public ResponseEntity<List<ItemResponseDto>> getMyItems(
            @RequestHeader("X-User-Id") UUID userId) {
        List<ItemResponseDto> items = itemService.findByOwnerId(userId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/neighbourhood/{neighbourhood}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByNeighbourhood(@PathVariable String neighbourhood) {
        List<ItemResponseDto> items = itemService.listByNeighborhood(neighbourhood);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/city/{city}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByCity(@PathVariable String city) {
        List<ItemResponseDto> items = itemService.listByCity(city);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/state/{state}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByState(@PathVariable String state) {
        List<ItemResponseDto> items = itemService.listByState(state);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/country/{country}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByCountry(@PathVariable String country) {
        List<ItemResponseDto> items = itemService.listByCountry(country);
        return ResponseEntity.ok(items);
    }

    //PAGINATED ENDPOINTS
    @GetMapping("/paginated")
    public ResponseEntity<PaginationResponseDto<ItemResponseDto>> getActiveItemsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponseDto<ItemResponseDto> response = itemService.listActiveItemsPaginated(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated/my-items")
    public ResponseEntity<PaginationResponseDto<ItemResponseDto>> getMyItems(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponseDto<ItemResponseDto> response = itemService.listByOwnerIdPaginated(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated/search/neighbourhood/{neighbourhood}")
    public ResponseEntity<PaginationResponseDto<ItemResponseDto>> getItemsByNeighbourhood(
            @PathVariable String neighbourhood,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponseDto<ItemResponseDto> response = itemService.listByNeighborhoodPaginated(neighbourhood, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated/search/city/{city}")
    public ResponseEntity<PaginationResponseDto<ItemResponseDto>> getItemsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponseDto<ItemResponseDto> response = itemService.listByCityPaginated(city, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated/search/state/{state}")
    public ResponseEntity<PaginationResponseDto<ItemResponseDto>> getItemsByState(
            @PathVariable String state,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponseDto<ItemResponseDto> response = itemService.listByStatePaginated(state, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated/search/country/{country}")
    public ResponseEntity<PaginationResponseDto<ItemResponseDto>> getItemsByCountry(
            @PathVariable String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponseDto<ItemResponseDto> response = itemService.listByCountryPaginated(country, page, size);
        return ResponseEntity.ok(response);
    }
}