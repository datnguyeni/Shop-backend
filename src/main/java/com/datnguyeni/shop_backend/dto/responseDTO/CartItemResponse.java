package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private Long cartId;  // id cart follow user
    private Long id;      // add follow number
    private Long variantId; // variant id
    private Long quantity;
    private Double basePrice;
    private String size;
    private String productName;
    private String imageUrl;
    private Double subTotal;

}
