package com.datnguyeni.shop_backend.service;


import com.datnguyeni.shop_backend.dto.requestDTO.CategoryCreationRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.CategoryCreationResponse;
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
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;

    }


    public List<CategoryResponse> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public List<CategoryResponse> getSubCategoriesBySlug(String slug) {
        Category parent = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return parent.getChildren().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryCreationResponse createCategory(CategoryCreationRequest request) {
        Category category = categoryMapper.toCategory(request);

        if (request.getParent_id() != null) {
            Category parent = categoryRepository.findById(request.getParent_id())
                    .orElseThrow(() -> new RuntimeException("Category cha không tồn tại"));
            category.setParent(parent);
        }

        category = categoryRepository.save(category);
        return categoryMapper.toCategoryCreationResponse(category);
    }

}