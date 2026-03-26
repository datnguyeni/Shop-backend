package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CategoryResponse;
import com.datnguyeni.shop_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/{id}/sub-categories")
    public ApiResponse<List<CategoryResponse>> getSubCategories(@PathVariable Long id) {

        List<CategoryResponse> responseList = categoryService.getSubCategories(id);

        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Get sub-categories successfully")
                .data(responseList)
                .build();
    }
}
