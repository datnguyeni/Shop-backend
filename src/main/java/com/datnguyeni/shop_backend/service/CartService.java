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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public  CartService(CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        ProductVariantRepository productVariantRepository,
                        CartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariantRepository = productVariantRepository;
        this.cartItemMapper = cartItemMapper;
    }


    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            log.info("Creating new cart for user {}", user.getId());

            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public CartItemResponse addToCart(AddToCartRequest request, User user) {
        Cart cart = this.getOrCreateCart(user);
        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể sản phẩm này"));

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndVariantId(cart.getId(), variant.getId());

        CartItem itemToSave;
        if (existingItemOpt.isPresent()) {
            itemToSave = existingItemOpt.get();
            int newQuantity = Math.toIntExact(itemToSave.getQuantity() + request.getQuantity());

            if (newQuantity > variant.getStockQuantity()) {
                throw new RuntimeException("Số lượng vượt quá sản phẩm có sẵn trong kho");
            }
            itemToSave.setQuantity((long) newQuantity);

        } else {
            if (request.getQuantity() > variant.getStockQuantity()) {
                throw new RuntimeException("Số lượng vượt quá sản phẩm có sẵn trong kho");
            }
            itemToSave = new CartItem();
            itemToSave.setCart(cart);
            itemToSave.setVariant(variant);
            itemToSave.setQuantity(request.getQuantity());
        }

        return cartItemMapper.toCartItemResponse(cartItemRepository.save(itemToSave));
    }


    @Transactional
    public CartItemResponse updateQuantity(Long cartItemId, int newQuantity, User user) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm này trong giỏ hàng"));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền thao tác trên giỏ hàng này");
        }

        if (newQuantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        // Validate: Kiểm tra tồn kho
        if (newQuantity > cartItem.getVariant().getStockQuantity()) {
            throw new RuntimeException("Số lượng vượt quá sản phẩm có sẵn trong kho");
        }

        cartItem.setQuantity((long) newQuantity);
        return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
    }

    @Transactional
    public void deleteCartItem(Long cartItemId, User user) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm này trong giỏ hàng"));

        // Bảo mật: Đảm bảo item này thuộc về user đang thao tác
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền thao tác trên giỏ hàng này");
        }

        cartItemRepository.delete(cartItem);
    }


    @Transactional
    public void clearCart(User user) {
        Cart cart = this.getOrCreateCart(user);
        cartItemRepository.deleteAllByCartId(cart.getId());
    }


}