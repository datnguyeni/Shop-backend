package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.AddToCartRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.CartItemResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CartResponse;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.repository.UserRepository;
import com.datnguyeni.shop_backend.service.CartService;
import com.datnguyeni.shop_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserRepository userRepository, UserService userService) {
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {

         User currentUser = userService.getCurrentUser();

        CartItemResponse response = cartService.addToCart(request, currentUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ApiResponse<CartResponse> getMyCart() {
        User currentUser = userService.getCurrentUser();

        CartResponse myCart = cartService.getCart(currentUser);

        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Lấy giỏ hàng thành công!")
                .data(myCart)
                .build();
    }

}
