package com.datnguyeni.shop_backend.dto.responseDTO;

import com.datnguyeni.shop_backend.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserResponse {

    private String email;
    private String fullName;
    private String phone;
    private String address;
    Set<Role> roles;
}
