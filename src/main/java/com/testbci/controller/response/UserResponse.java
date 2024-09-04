package com.testbci.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private UUID id;
    private String token;
    private boolean isActive;
    private LocalDateTime created;
    private LocalDateTime lastLogin;

}
