package com.segatto_builder.tinyvillagehubitemservice.controller;

import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/items/admin")
@RequiredArgsConstructor
public class ItemAdminController {

    private final IService itemService;

    //Might be helpful
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreItem(@PathVariable String id,
                                            @RequestHeader("X-Admin-Id") UUID adminId) {
        itemService.updateStatus(id, Status.INACTIVE, adminId);
        return ResponseEntity.ok().build();
    }
}

