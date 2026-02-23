package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.dto.requestDTO.UserCreationRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.UserUpdateRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.UserResponse;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.entity.enums.UserStatus;
import com.datnguyeni.shop_backend.mapper.UserMapper;
import com.datnguyeni.shop_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse addUser(UserCreationRequest request) {

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, userUpdateRequest);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

}
