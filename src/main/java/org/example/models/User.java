package org.example.models;

import lombok.Builder;
import lombok.Data;
import org.example.models.enums.Role;
import org.example.models.enums.UserStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private Long id;
    private String email;
    private String hashPassword;
    private String fullName;
    private Integer course;
    private String about;
    private String contact;
    private LocalDateTime createdAt;
    private Role role;
    private UserStatus userStatus;
    private Integer cityId;
    private Integer groupId;
    private Integer avatarId;
}
