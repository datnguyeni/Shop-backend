package com.datnguyeni.shop_backend.dto.responseDTO;


import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class ProductImageResponse {

    private String imageUrl;
    private boolean defaultValue;
    private String color;

}
