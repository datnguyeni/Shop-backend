package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // Lấy danh sách kèm ảnh để tối ưu query
    @EntityGraph(attributePaths = {"images"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    // Truy vấn sản phẩm dựa trên slug của Category thông qua join bảng
    @EntityGraph(attributePaths = {"images"})
    Page<Product> findByCategory_Slug(String slug, Pageable pageable);

    @EntityGraph(attributePaths = {"images", "variants", "category"})
    Optional<Product> findById(Long id);

}