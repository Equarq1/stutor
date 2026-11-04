package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationDto {
    private String email;
    private String password;
    private String fullName;
    private Integer cityId;
    private Integer groupId;
}
