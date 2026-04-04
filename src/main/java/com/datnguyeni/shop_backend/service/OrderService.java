package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.dto.requestDTO.OrderRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.OrderResponse;
import com.datnguyeni.shop_backend.entity.*;
import com.datnguyeni.shop_backend.mapper.OrderItemMapper;
import com.datnguyeni.shop_backend.mapper.OrderMapper;
import com.datnguyeni.shop_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        double totalAmount = 0.0;

        // 3. Xử lý từng món hàng trong giỏ
        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getVariant();

            // Kiểm tra tồn kho
            if (variant.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm mã " + variant.getSku() + " không đủ số lượng trong kho!");
            }

            // Trừ tồn kho và lưu lại
            variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
            variantRepository.save(variant);

            // Map từ CartItem sang OrderItem
            OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);

            //  set Order cha cho OrderItem con
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);

            // Cộng dồn tổng tiền ( dùng luôn hàm getSubTotal trong CartItem)
            totalAmount += cartItem.getSubTotal();
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