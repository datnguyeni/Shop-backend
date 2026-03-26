package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.dto.requestDTO.AddToCartRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.CartItemResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CartResponse;
import com.datnguyeni.shop_backend.entity.Cart;
import com.datnguyeni.shop_backend.entity.CartItem;
import com.datnguyeni.shop_backend.entity.ProductVariant;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.mapper.CartItemMapper;
import com.datnguyeni.shop_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductVariantRepository productVariantRepository,
                      CartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariantRepository = productVariantRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public Cart getOrCreateCart(User user) {
        Optional<Cart> existingCart = cartRepository.findByUser(user);

      if(existingCart.isPresent()){
          return existingCart.get();
      }

      Cart cart = new Cart();
      cart.setUser(user);

      log.info("Creating new cart for user {}", user.getId());
      return cartRepository.save(cart);
    }


    @Transactional
    public CartItemResponse addToCart(AddToCartRequest addToCartRequest, User user) {

        Cart cart = this.getOrCreateCart(user);

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndVariantId(
                cart.getId(),
                addToCartRequest.getVariantId()
        );

        CartItem finalItem;
        if (existingItem.isPresent()) {
            finalItem = existingItem.get();
            finalItem.setQuantity(finalItem.getQuantity() + addToCartRequest.getQuantity());
        } else {
            ProductVariant productVariant = productVariantRepository.findById(addToCartRequest.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found"));

            finalItem = new CartItem();
            finalItem.setQuantity(addToCartRequest.getQuantity());
            finalItem.setCart(cart);
            finalItem.setVariant(productVariant);
        }

        CartItem savedItem = cartItemRepository.save(finalItem);

        return cartItemMapper.toCartItemResponse(savedItem);
    }

    public CartResponse getCart(User user) {

        Cart cart = this.getOrCreateCart(user);
        List<CartItem> cartItems = cart.getCartItems();

        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(cartItemMapper::toCartItemResponse)
                .toList();

        double totalCartPrice = itemResponses.stream()
                .mapToDouble(CartItemResponse::getSubTotal)
                .sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .totalCartPrice(totalCartPrice)
                .build();
    }

    public void deleteCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm này trong giỏ hàng"));

        cartItemRepository.delete(cartItem);

    }

}

