package com.datnguyeni.shop_backend.dto.responseDTO;

import com.datnguyeni.shop_backend.Projection.RevenueChartProjection;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class RevenueResponse {

    BigDecimal totalAmount;
    Long totalOrders;
    Long totalOrderItemsSold;

    private List<RevenueChartProjection> chartData;


}
