package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.configuration.VNPayConfig;
import com.datnguyeni.shop_backend.entity.Order;
import com.datnguyeni.shop_backend.entity.Payment;
import com.datnguyeni.shop_backend.repository.OrderRepository;
import com.datnguyeni.shop_backend.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public String createVnPayPaymentUrl(Long orderId, HttpServletRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

        // 2. Khởi tạo theo tài liệu  VNPay
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = String.valueOf(order.getId()); // Mã giao dịch
        String vnp_IpAddr = vnPayConfig.getIpAddress(request); // Lấy IP của user
        String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();
        String vnp_OrderInfo = "Thanh toan don hang ma: " + order.getId();
        String vnp_OrderType = "other";

        BigDecimal amount = order.getTotalAmount().multiply(BigDecimal.valueOf(100));

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Đơn hàng có giá trị thanh toán trong 15 phút
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // 4. BẮT BUỘC: Sắp xếp các tham số theo thứ tự Alphabet
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        // 5. Lắp ráp chuỗi dữ liệu (query) và tạo chữ ký (hashData)
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);

            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // 6. Dùng hàm hmacSHA512 để băm dữ liệu cùng SecretKey -> Tạo vnp_SecureHash
        String queryUrl = query.toString();
        String vnp_SecureHash = vnPayConfig.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }

    /**
     * Hàm hứng dữ liệu trả về từ VNPay
     * Trả về true nếu thanh toán thành công và đã lưu, false nếu thất bại hoặc sai chữ ký
     *
     * Lưu đơn hàng vào dbo payment
     */

    public boolean handleVnPayCallback(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        // Lấy chữ ký từ request để đối chiếu
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        //  bỏ hash ra khỏi map trước khi tạo lại chữ ký
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }

        // Tạo lại chữ ký từ các tham số trả về
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        try {
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = fields.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }
        } catch (Exception e) {
            return false; // Lỗi encode
        }

        String signValue = vnPayConfig.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());

        // Đối chiếu chữ ký
        if (signValue.equals(vnp_SecureHash)) {
            // Chữ ký hợp lệ -> Kiểm tra trạng thái giao dịch
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {

                Long orderId = Long.parseLong(request.getParameter("vnp_TxnRef"));

                Order order = orderRepository.findById(orderId).orElse(null);
                if (order != null) {

                    // 1. Lưu vào bảng Payment
                    Payment payment = new Payment();
                    payment.setOrder(order);
                    payment.setMethod("VNPAY");
                    payment.setStatus("PAID");


                    paymentRepository.save(payment);

                    // 2. Cập nhật trạng thái cho Order
                    order.setStatus("SHIPPING");
                    orderRepository.save(order);

                    return true;
                }
            }
        }

        return false;
    }

}