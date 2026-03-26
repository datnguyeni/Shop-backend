package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.entity.Cart;
import com.datnguyeni.shop_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository  extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUser(User user);

}
