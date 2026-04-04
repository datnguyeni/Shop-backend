package com.datnguyeni.shop_backend.mapper;

import com.datnguyeni.shop_backend.dto.responseDTO.CartItemResponse;
import com.datnguyeni.shop_backend.entity.CartItem;
import com.datnguyeni.shop_backend.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "variant.id", target = "variantId")

    @Mapping(source = "cart.id", target = "cartId")

    @Mapping(source = "variant.size", target = "size")
    @Mapping(source = "variant.product.name", target = "productName")
    @Mapping(source = "variant.product.basePrice", target = "basePrice")
    @Mapping(source = "variant.defaultImageUrl", target = "imageUrl")
    @Mapping(source= "variant.color", target ="color")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toItemResponseList(List<CartItem> items);
}