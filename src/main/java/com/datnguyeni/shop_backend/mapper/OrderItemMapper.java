package com.datnguyeni.shop_backend.mapper;

import com.datnguyeni.shop_backend.dto.requestDTO.OrderRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.OrderItemResponse;
import com.datnguyeni.shop_backend.entity.CartItem;
import com.datnguyeni.shop_backend.entity.Order;
import com.datnguyeni.shop_backend.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {


    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variant.product.name", target = "productName")
    @Mapping(source = "variant.size", target = "size")
    @Mapping(source = "variant.color", target = "color")
    @Mapping(source = "variant.defaultImageUrl", target = "imageUrl")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);


    @Mapping(target = "id", ignore = true)      // ID của order_item
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "variant", target = "variant")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "variant.product.basePrice", target = "price")
    OrderItem toOrderItem(CartItem cartItem);

}
