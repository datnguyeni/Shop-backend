package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.Projection.RevenueChartProjection;
import com.datnguyeni.shop_backend.dto.responseDTO.RevenueResponse;
import com.datnguyeni.shop_backend.mapper.OrderItemMapper;
import com.datnguyeni.shop_backend.mapper.OrderMapper;
import com.datnguyeni.shop_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    public RevenueResponse getDashboardStats(String period) {
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

        switch (period) {
            case "this_month":
                startDate = LocalDate.now().withDayOfMonth(1);
                break;
            case "this_year":
                startDate = LocalDate.now().withDayOfYear(1);
                break;
            case "this_week":
            default:
                startDate = LocalDate.now().minusDays(7);
                break;
        }

        // Convert sang LocalDateTime để quét trọn vẹn từ 00h00 đến 23h59
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);


        BigDecimal totalRev = orderRepository.getTotalAmount();
        Long totalOrdersCount = orderRepository.getTotalOrders();
        Long totalItems = orderItemRepository.getTotalQuantity();

        List<RevenueChartProjection> chartData = orderRepository.getRevenueChartData(startDateTime, endDateTime);

        return RevenueResponse.builder()
                .totalAmount(totalRev != null ? totalRev : BigDecimal.ZERO)
                .totalOrders(totalOrdersCount != null ? totalOrdersCount : 0L)
                .totalOrderItemsSold(totalItems != null ? totalItems : 0L)
                .chartData(chartData)
                .build();
    }


}
