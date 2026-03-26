package com.datnguyeni.shop_backend.dto.responseDTO;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class CartResponse {

    private Long cartId;
    private List<CartItemResponse> items;
    private Double totalCartPrice;

}
