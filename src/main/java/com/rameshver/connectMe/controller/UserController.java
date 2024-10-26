package com.rameshver.connectMe.controller;

import com.rameshver.connectMe.dto.*;
import com.rameshver.connectMe.response.ApiResponse;
import com.rameshver.connectMe.service.userService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterUser user)
    {
        try
        {
            UserDto registeredUser = userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Register user done!", registeredUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

//    @PostMapping("login")
//    public ResponseEntity<ApiResponse> login(@RequestBody LoginUser user)
//    {
//        try
//        {
//            UserDto loginUser = userService.login(user);
//            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Login successfull", loginUser));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id)
    {
        try
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Log or print the authentication details
            System.out.println("Authentication Type: " + authentication.getClass().getSimpleName());
            System.out.println("Username: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());
            UserDto registeredUser = userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User get By id Success", registeredUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/{userId}/follow/{followUserId}")
    public ResponseEntity<ApiResponse> followUser(@PathVariable Long userId, @PathVariable Long followUserId){
        try
        {
            UserDto user = userService.followUser(userId, followUserId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Follow user done!", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/{userId}/unfollow/{unFollowUserId}")
    public ResponseEntity<ApiResponse> unFollowUser(@PathVariable Long userId, @PathVariable Long unFollowUserId){
        try
        {
            UserDto user = userService.unFollowUser(userId, unFollowUserId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Unfollow user done", user));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<ApiResponse> getFollowers(@PathVariable Long userId){
        try
        {
            Set<FollowerDto> followers = userService.getFollowers(userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get followers details success!", followers));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<ApiResponse> getFollowing(@PathVariable Long userId){
        try
        {
            Set<FollowerDto> following = userService.getFollowing(userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get followings details success!", following));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllUser(){
        try {
            List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get All Users Success!", users));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}/update")
    public ResponseEntity<ApiResponse> updateProfile(@PathVariable Long id, @RequestBody UserUpdateDto user)
    {
        try {
            UserDto updatedUser = userService.updateProfile(id, user);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get All Users Success!", updatedUser));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
