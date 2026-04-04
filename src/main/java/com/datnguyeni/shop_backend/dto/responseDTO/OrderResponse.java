package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class OrderResponse {

    private Long id;        // Mã đơn hàng (order_id)
    private String address;
    private String phone;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;

    private List<OrderItemResponse> orderItems;

}
