package com.datnguyeni.shop_backend.dto.requestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_REQUIRED")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "INVALID_PHONE")
    private String phone;

    private String lastName;
    private String firstName;

}
