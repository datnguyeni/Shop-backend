package com.datnguyeni.shop_backend.dto.requestDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {

    String token;
}
