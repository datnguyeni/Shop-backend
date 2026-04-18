package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantCreationResponse {

    Long productId;
    String size;
    Long stockQuantity;
    String sku;
    String color;

    private List<ProductImageResponse> images;

}
