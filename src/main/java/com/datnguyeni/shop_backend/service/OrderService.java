package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.Projection.RevenueChartProjection;
import com.datnguyeni.shop_backend.dto.requestDTO.OrderRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.OrderResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.RevenueResponse;
import com.datnguyeni.shop_backend.entity.*;
import com.datnguyeni.shop_backend.mapper.OrderItemMapper;
import com.datnguyeni.shop_backend.mapper.OrderMapper;
import com.datnguyeni.shop_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductVariantRepository variantRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;

    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(User user, OrderRequest request) {

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng của user này"));

        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống, không thể thanh toán");
        }

        // 2. Khởi tạo Order mới
        Order order = new Order();
        order.setUser(user);
        order.setAddress(request.getAddress());
        order.setPhone(request.getPhone());
        order.setStatus("PENDING");
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Xử lý từng món hàng trong giỏ
        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getVariant();

            if (variant.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm mã " + variant.getSku() + " không đủ số lượng trong kho!");
            }

            variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
            variantRepository.save(variant);

            OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);

            totalAmount = totalAmount.add(cartItem.getSubTotal());
        }

        order.setTotalAmount(totalAmount);

        // 5. Lưu đơn hàng CascadeType.ALL
        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return orderMapper.toOrderResponse(savedOrder);
    }


    public OrderResponse getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng mã : " + orderId));

        return orderMapper.toOrderResponse(order);
    }

}