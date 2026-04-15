package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.*;
import org.mapstruct.Mapping;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class OrderItemResponse {
    private Long id;            // ID của order_item
    private Long variantId;     // ID của phiên bản sản phẩm

    private String productName;
    private String size;
    private String color;

    private int quantity;
    private Double price;       // Giá tại thời điểm mua
    private String imageUrl;

}
