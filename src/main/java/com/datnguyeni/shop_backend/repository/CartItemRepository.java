package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.entity.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @EntityGraph(attributePaths = {"variant"})
    Optional<CartItem> findById(Long id);
    
    @EntityGraph(attributePaths = {"variant"})
    Optional<CartItem> findByCartIdAndVariantId(Long cartId, Long variantId);

    // 3. THÊM MỚI: Dùng cho chức năng xóa sạch giỏ hàng (Clear Cart)
    // Phải có @Modifying khi viết custom query làm thay đổi dữ liệu (Insert/Update/Delete)
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);

}