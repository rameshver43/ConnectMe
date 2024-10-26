package com.rameshver.connectMe.service.userService;

import com.rameshver.connectMe.dto.*;
import com.rameshver.connectMe.enums.UserRoles;
import com.rameshver.connectMe.enums.UserStatus;
import com.rameshver.connectMe.model.User;
import com.rameshver.connectMe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
@Transactional
public class UserService implements IUserService{

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserDto register(RegisterUser user) {

        if(userRepository.findByUsername(user.getUsername()).isPresent())
        {
            throw new RuntimeException("Username already exists");
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent())
        {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setStatus(UserStatus.ACTIVE);
        newUser.getRoles().add(UserRoles.USER);
        User savedUser =  userRepository.save(newUser);
        return getUserDtoFromUser(savedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user  = userRepository.findById(id);
        if(user.isEmpty())
        {
            throw new RuntimeException("User not found!");
        }
        return getUserDtoFromUser(user.get());

    }

    @Override
    public UserDto getUserByUsername(String username) {
        Optional<User> user  = userRepository.findByUsername(username);
        if(user.isEmpty())
        {
            throw new RuntimeException("User not found!");
        }
        return getUserDtoFromUser(user.get());
    }

    @Override
    public UserDto followUser(Long userId, Long followUserId) {
        Optional<User> user1 = userRepository.findById(userId);
        Optional<User> user2 = userRepository.findById(followUserId);
        if(user1.isEmpty() || user2.isEmpty())
        {
            throw new RuntimeException("User not found!");
        }
        if(user1.equals(user2))
        {
            throw new RuntimeException("Both Users are equal!");
        }
        Set<User> user2Followers = user2.get().getFollowers();
        if(user2Followers.contains(user1.get()))
        {
            throw new RuntimeException("Already follow!");
        }
        user2Followers.add(user1.get());
        user2.get().setFollowers(user2Followers);

        Set<User> user1Following = user1.get().getFollowing();
        user1Following.add(user2.get());
        user1.get().setFollowing(user1Following);

        User savedUser = userRepository.save(user1.get());
        userRepository.save(user2.get());
        return getUserDtoFromUser(savedUser);
    }

    @Override
    public UserDto unFollowUser(Long userId, Long unFollowUserId) {
        Optional<User> user1 = userRepository.findById(userId);
        Optional<User> user2 = userRepository.findById(unFollowUserId);
        if(user1.isEmpty() || user2.isEmpty())
        {
            throw new RuntimeException("User not found!");
        }
        if(user1.equals(user2))
        {
            throw new RuntimeException("Both Users are equal!");
        }
        Set<User> user2Followers = user2.get().getFollowers();
        if(!user2Followers.contains(user1.get()))
        {
            throw new RuntimeException("Follower not found!");
        }
        user2Followers.remove(user1.get());
        user2.get().setFollowers(user2Followers);

        Set<User> user1Following = user1.get().getFollowing();
        user1Following.remove(user2.get());
        user1.get().setFollowing(user1Following);

        User savedUser = userRepository.save(user1.get());
        userRepository.save(user2.get());
        return getUserDtoFromUser(savedUser);
    }

    @Override
    public Set<FollowerDto> getFollowers(Long userId) {
        Optional<User> user  = userRepository.findById(userId);
        if(user.isEmpty())
        {
            throw new RuntimeException("User not found!");
        }

        return getFollowerDto(user.get().getFollowers());
    }

    @Override
    public Set<FollowerDto> getFollowing(Long userId) {
        Optional<User> user  = userRepository.findById(userId);
        if(user.isEmpty())
        {
            throw new RuntimeException("User not found!");
        }

        return getFollowerDto(user.get().getFollowing());
    }

    private Set<FollowerDto> getFollowerDto(Set<User> users) {
        Set<FollowerDto> followersDto = new HashSet<>();

        for(User user: users)
        {
            FollowerDto followerDto = new FollowerDto();
            followerDto.setId(user.getId());
            followerDto.setUsername(user.getUsername());
            followersDto.add(followerDto);
        }

        return  followersDto;
    }

    private UserDto getUserDtoFromUser(User user)
    {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setBio(user.getBio());
        userDto.setFollowing(getFollowerDto(user.getFollowing()));
        userDto.setFollowers(getFollowerDto(user.getFollowers()));
        userDto.setStatus(user.getStatus());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setLastLogin(user.getLastLogin());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user: users){
            userDtos.add(getUserDtoFromUser(user));
        }
        return userDtos;
    }

    public UserDto updateProfile(Long id, UserUpdateDto user){
        Optional<User> dbUser  = userRepository.findById(id);
        if(dbUser.isEmpty())
        {
            throw new RuntimeException("User not found!");
        }
        dbUser.get().setBio(user.getBio());
        dbUser.get().setProfilePictureUrl(user.getProfilePictureUrl());
        dbUser.get().setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(dbUser.get());
        return getUserDtoFromUser(savedUser);
    }

}
