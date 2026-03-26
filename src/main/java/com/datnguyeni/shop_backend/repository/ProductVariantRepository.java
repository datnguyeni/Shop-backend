package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {

    Optional<ProductVariant> findById(Long id);

}
