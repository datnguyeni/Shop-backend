package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class ProductsResponse {

    private  String id;

    private String name;
    private String basePrice;

    private Set<ProductImageResponse> images;

    private CategoryResponse category;

}
