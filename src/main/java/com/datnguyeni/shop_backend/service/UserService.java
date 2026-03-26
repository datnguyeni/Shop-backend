package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.dto.requestDTO.UserCreationRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.UserUpdateRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.UserResponse;
import com.datnguyeni.shop_backend.entity.Role;
import com.datnguyeni.shop_backend.entity.User;

import com.datnguyeni.shop_backend.entity.enums.RoleType;
import com.datnguyeni.shop_backend.entity.enums.UserStatus;
import com.datnguyeni.shop_backend.mapper.UserMapper;
import com.datnguyeni.shop_backend.repository.RoleRepository;
import com.datnguyeni.shop_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasRole('ADMIN')") // == ROLE_ADMIN
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
        user.setEmail(request.getEmail().toLowerCase().trim());

        // 1 user has many roles
        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }


    @Transactional
    public UserResponse updateMyProfile(UserUpdateRequest request) {

        User currentUser = getCurrentUser();
        userMapper.updateUser(currentUser, request);

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lỗi: Tài khoản không tồn tại trong hệ thống!"));

        return loggedInUser;
    }


}
