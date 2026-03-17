package com.datnguyeni.shop_backend.dto.responseDTO;


import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class ProductVariantResponse {

        private Long id;
        private String size;
        private String color;
        private String sku;
        private Long stockQuantity;


}
