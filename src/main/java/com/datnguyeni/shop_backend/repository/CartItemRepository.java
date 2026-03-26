package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface  CartItemRepository extends JpaRepository<CartItem,Long> {

//    @EntityGraph(attributePaths = {"variant"})
//    Optional<CartItem> findById(long id);

    // Tìm CartItem dựa vào ID Giỏ hàng VÀ ID Món hàng
    Optional<CartItem> findByCartIdAndVariantId(Long cartId, Long variantId);

}
