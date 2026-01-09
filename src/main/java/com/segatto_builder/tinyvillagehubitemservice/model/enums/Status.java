package com.segatto_builder.tinyvillagehubitemservice.model.enums;

public enum Status {
    ACTIVE,         // Live and visible to others
    INACTIVE,       // User removed from marketplace (but not deleted)
    PENDING,        // Someone is negotiating/interested
    COMPLETED,      // Successfully traded/donated
    DELETED,        // Soft Delete/User won't see it anymore
}