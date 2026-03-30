package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.AddToCartRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CartItemResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CartResponse;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.service.CartService;
import com.datnguyeni.shop_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")

public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // 1. Lấy toàn bộ giỏ hàng
    @GetMapping
    public ApiResponse<CartResponse> getMyCart() {
        User currentUser = userService.getCurrentUser();
        CartResponse myCart = cartService.getCart(currentUser);

        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Lấy giỏ hàng thành công")
                .data(myCart)
                .build();
    }

    @PostMapping("/items/add")
    public ApiResponse<CartItemResponse> addToCart(@RequestBody AddToCartRequest request) {
        User currentUser = userService.getCurrentUser();
        CartItemResponse response = cartService.addToCart(request, currentUser);

        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .message("Thêm vào giỏ hàng thành công")
                .data(response)
                .build();
    }


    @PutMapping("/items/{itemId}")
    public ApiResponse<CartItemResponse> updateQuantity(
            @PathVariable Long itemId,
            @RequestParam int quantity) {

        User currentUser = userService.getCurrentUser();
        CartItemResponse response = cartService.updateQuantity(itemId, quantity, currentUser);

        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .message("Cập nhật số lượng thành công")
                .data(response)
                .build();
    }


    @DeleteMapping("/items/{itemId}")
    public ApiResponse<Void> deleteCartItem(@PathVariable Long itemId) {
        User currentUser = userService.getCurrentUser();
        cartService.deleteCartItem(itemId, currentUser);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa sản phẩm khỏi giỏ thành công")
                .build();
    }

    @DeleteMapping("/clear")
    public ApiResponse<Void> clearCart() {
        User currentUser = userService.getCurrentUser();
        cartService.clearCart(currentUser);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa toàn bộ giỏ hàng thành công")
                .build();
    }
}