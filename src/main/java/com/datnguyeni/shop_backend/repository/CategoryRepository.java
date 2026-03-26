package com.datnguyeni.shop_backend.repository;


import com.datnguyeni.shop_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByParentId(Long parentId);

}
