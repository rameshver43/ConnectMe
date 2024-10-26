package com.rameshver.connectMe.dto;

import com.rameshver.connectMe.enums.UserRoles;
import com.rameshver.connectMe.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String bio = "";

    private String profilePictureUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<FollowerDto> followers = new HashSet<>();

    private Set<FollowerDto> following = new HashSet<>();

    private UserStatus status;

    private LocalDateTime lastLogin;

    private Set<UserRoles> roles;
}
