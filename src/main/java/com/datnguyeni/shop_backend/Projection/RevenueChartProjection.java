package com.datnguyeni.shop_backend.Projection;

import java.math.BigDecimal;

public interface RevenueChartProjection {
    String getDate();        // Trục X (Ví dụ: "2026-04-08")
    BigDecimal getRevenue(); // Trục Y (Ví dụ: 15000000)
}