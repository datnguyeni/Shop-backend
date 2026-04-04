package com.datnguyeni.shop_backend.mapper;

import com.datnguyeni.shop_backend.dto.responseDTO.OrderResponse;
import com.datnguyeni.shop_backend.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponse toOrderResponse(Order order);
}