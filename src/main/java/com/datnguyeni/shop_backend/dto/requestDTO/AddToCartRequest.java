package com.datnguyeni.shop_backend.dto.requestDTO;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    private Long variantId;
    private Long quantity;
    private String size;
    private String color;
}
