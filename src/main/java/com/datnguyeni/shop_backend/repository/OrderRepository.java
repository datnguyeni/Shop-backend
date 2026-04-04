package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.dto.responseDTO.OrderResponse;
import com.datnguyeni.shop_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
