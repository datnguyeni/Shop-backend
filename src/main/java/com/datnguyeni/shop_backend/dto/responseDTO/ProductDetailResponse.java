package com.datnguyeni.shop_backend.dto.responseDTO;


import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class ProductDetailResponse {

    private Long id;

    private String name;

    private String description;

    private Double basePrice;

    private Set<ProductImageResponse> images;

    private Set<ProductVariantResponse> variants;

}