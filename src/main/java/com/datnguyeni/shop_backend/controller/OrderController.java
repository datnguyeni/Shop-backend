package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.OrderRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.OrderResponse;
import com.datnguyeni.shop_backend.entity.Order;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.service.OrderService;
import com.datnguyeni.shop_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {

        User currentUser = userService.getCurrentUser();

        OrderResponse orderResponse = orderService.createOrder(currentUser, request);

        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("Tạo đơn hàng thành công")
                .data(orderResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderDetails(@PathVariable Long id) {

        OrderResponse orderResponse = orderService.getOrderDetails(id);

        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("lay don hang thanh cong")
                .data(orderResponse)
                .build();
    }



}