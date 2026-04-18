package com.datnguyeni.shop_backend.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantCreationRequest {

    Long productId;
    Long stockQuantity;
    String size;
    String sku;
    String color;

}
