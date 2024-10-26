package com.rameshver.connectMe.service.userService;


import com.rameshver.connectMe.dto.*;

import java.util.List;
import java.util.Set;

public interface IUserService {
    UserDto register(RegisterUser user);
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    UserDto followUser(Long userId, Long followUserId);
    UserDto unFollowUser(Long userId, Long unFollowUserId);
    Set<FollowerDto> getFollowers(Long userId);
    Set<FollowerDto> getFollowing(Long userId);
    List<UserDto> getAllUsers();
    UserDto updateProfile(Long id, UserUpdateDto user);
}
