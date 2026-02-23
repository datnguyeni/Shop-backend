package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.UserCreationRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.UserUpdateRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.UserResponse;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.mapper.UserMapper;
import com.datnguyeni.shop_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserMapper userMapper;

    @Autowired
    public UserController (UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest userCreationRequest) {

        UserResponse createdUser = userService.addUser(userCreationRequest);

        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("User created successfully")
                .data(createdUser)
                .build();
    }

    @GetMapping("/get-all")
    public ApiResponse<List<UserResponse>> getAllUsers() {

        List<UserResponse> userResponseList = userService.findAll();

        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Get all users successfully")
                .data(userResponseList)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {

        UserResponse updatedUser = userService.updateUser(id, userUpdateRequest);

        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User updated successfully")
                .data(updatedUser)
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .code(204)
                .message("User has been deleted")
                .build();
    }
}
