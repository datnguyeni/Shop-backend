package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class LoginResponse {
    private String accessToken;
    private String tokenType;
}