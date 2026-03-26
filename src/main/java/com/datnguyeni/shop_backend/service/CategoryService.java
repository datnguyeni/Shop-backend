package com.datnguyeni.shop_backend.service;


import com.datnguyeni.shop_backend.dto.responseDTO.CategoryResponse;
import com.datnguyeni.shop_backend.entity.Category;
import com.datnguyeni.shop_backend.mapper.CategoryMapper;
import com.datnguyeni.shop_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,  CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> getSubCategories(Long parentId) {
        // 1. Lấy danh sách Entity từ DB
        List<Category> children = categoryRepository.findByParentId(parentId);

        // 2. Dùng Stream để lặp và nhờ categoryMapper dịch sang DTO
        return children.stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }



}
