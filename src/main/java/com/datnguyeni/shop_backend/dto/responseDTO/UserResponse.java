package com.datnguyeni.shop_backend.dto.responseDTO;

import com.datnguyeni.shop_backend.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponse {
    private String email;
    private String lastname;
    private String firstname;
    private String phone;
}
