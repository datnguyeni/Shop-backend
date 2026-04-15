package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/vnpay/create-url")
    public ApiResponse<String> createPaymentUrl(
            @RequestParam Long orderId,
            HttpServletRequest request) {

        String paymentUrl = paymentService.createVnPayPaymentUrl(orderId, request);

        return ApiResponse.<String>builder()
                .code(200)
                .message("Tạo link thanh toán VNPay thành công")
                .data(paymentUrl)
                .build();
    }

    @GetMapping("/vnpay-return")
    public void vnPayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isSuccess = paymentService.handleVnPayCallback(request);
        String orderId = request.getParameter("vnp_TxnRef"); // Lấy mã đơn hàng từ VNPay trả về

        if (isSuccess) {
            response.sendRedirect("http://127.0.0.1:5500/order-detail.html?orderId=" + orderId);
        } else {
            response.sendRedirect("http://127.0.0.1:5500/cart.html?status=failed");
        }

    }


}