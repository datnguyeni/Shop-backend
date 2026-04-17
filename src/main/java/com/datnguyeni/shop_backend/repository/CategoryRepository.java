package com.datnguyeni.shop_backend.repository;


import com.datnguyeni.shop_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    List<Category> findByParentIsNull();


    Optional<Category> findBySlug(String slug);

    Boolean existsBySlug(String slug);

    Optional<Category> findByParentId(Long id);
}