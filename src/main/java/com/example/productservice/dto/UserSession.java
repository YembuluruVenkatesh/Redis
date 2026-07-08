package com.example.productservice.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {

    private String sessionId;

    private String username;

    private LocalDateTime loginTime;

    private LocalDateTime lastAccessTime;

}