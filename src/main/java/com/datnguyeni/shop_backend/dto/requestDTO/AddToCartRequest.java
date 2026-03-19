package com.datnguyeni.shop_backend.dto.requestDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {

    private Long variantId;
    private Long quantity;

}
