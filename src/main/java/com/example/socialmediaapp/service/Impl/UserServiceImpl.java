package com.example.socialmediaapp.service.Impl;

import com.example.socialmediaapp.dto.UserProfileDto;
import com.example.socialmediaapp.entities.User;
import com.example.socialmediaapp.mapstruct.MapStructMapper;
import com.example.socialmediaapp.repository.UserRepository;
import com.example.socialmediaapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapStructMapper mapStructMapper;

    public UserServiceImpl(UserRepository userRepository, MapStructMapper mapStructMapper) {
        this.userRepository = userRepository;
        this.mapStructMapper = mapStructMapper;
    }

    @Override
    public User addUser(User user) {
        User savedUser = userRepository.save(user);
        log.info("User saved successfully");
        return savedUser;
    }

    @Override
    public List<UserProfileDto> getAllUsers() {
        List<UserProfileDto> userProfileDtoList = userRepository.findAll()
                .stream()
                .map(mapStructMapper::userToUserProfileDto)
                .collect(Collectors.toList());
        log.info("User loaded successfully");
        return userProfileDtoList;
    }

    @Override
    public int updateUser(User user) {
        User updatedUser = userRepository.findById(user.getUserId()).orElse(null);
        if (updatedUser == null) {
            log.warn("User with id = " + user.getUserId() + " doesn't exist");
            return 0;
        }
        int status = userRepository.updateUser(user.getFirstName(), user.getUserId());
        log.info("User updated successfully");
        return status;
    }

    @Override
    public String deleteUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User with id = " + userId + " doesn't exist");
            return "Fail";
        }
        userRepository.deleteById(userId);
        log.info("User deleted successfully");
        return "Success";
    }

}
