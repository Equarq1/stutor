package org.example.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    String email;
    String password;
}
