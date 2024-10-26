package com.rameshver.connectMe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rameshver.connectMe.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserUpdateDto {
    private String bio;
    private String profilePictureUrl;
}
