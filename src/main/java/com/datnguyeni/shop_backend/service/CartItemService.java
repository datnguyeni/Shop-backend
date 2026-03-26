package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.dto.responseDTO.CartItemResponse;
import com.datnguyeni.shop_backend.mapper.CartItemMapper;
import com.datnguyeni.shop_backend.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository,  CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public Optional<CartItemResponse> getProductVariantById(long id) {

        return cartItemRepository.findById(id)
                .map(cartItemMapper::toCartItemResponse);

    }

}
