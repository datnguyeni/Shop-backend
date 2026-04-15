package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.Projection.RevenueChartProjection;
import com.datnguyeni.shop_backend.dto.responseDTO.OrderResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.RevenueResponse;
import com.datnguyeni.shop_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

//    @Query("SELECT new com.datnguyeni.shop_backend.dto.responseDTO.RevenueResponse" +
//            " (SUM(o.totalAmount), COUNT(o)) FROM Order o")
//    RevenueResponse getRevenueStats();

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal getTotalAmount();

    @Query("SELECT COUNT(o) FROM Order o")
    Long getTotalOrders();

    @Query(value =
            "SELECT CAST(created_at AS DATE) AS date, " +
                    "SUM(total_amount) AS revenue " +
                    "FROM orders " +                      //  tên bảng thật trong DB
                    "WHERE created_at >= :startDate AND created_at <= :endDate " +
                    "GROUP BY CAST(created_at AS DATE) " +
                    "ORDER BY CAST(created_at AS DATE) ASC",
            nativeQuery = true)
    List<RevenueChartProjection> getRevenueChartData(@Param("startDate") LocalDateTime start, @Param("endDate") LocalDateTime end);
    //Repository (truyền param vào query)

}
