package com.datnguyeni.shop_backend.repository;

import com.datnguyeni.shop_backend.entity.Product;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {


    @EntityGraph(attributePaths = {"images", "variants", "category"})
    Optional<Product> findById(Long id);


    @EntityGraph(attributePaths = {"images"})
    Page<Product> findAll(Pageable pageable);




}
