package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.CategoryCreationRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CategoryCreationResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CategoryResponse;
import com.datnguyeni.shop_backend.entity.Category;
import com.datnguyeni.shop_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/menu")
    public ApiResponse<List<CategoryResponse>> getMenu() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Get menu tree successfully")
                .data(categoryService.getCategoryTree())
                .build();
    }


    @GetMapping("/{slug}")
    public ApiResponse<List<CategoryResponse>> getSubCategories(@PathVariable String slug) {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Get sub-categories successfully")
                .data(categoryService.getSubCategoriesBySlug(slug))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<CategoryCreationResponse>  createCategory(@RequestBody CategoryCreationRequest request){

        CategoryCreationResponse createdCategory = categoryService.createCategory(request);

        return ApiResponse.<CategoryCreationResponse>builder()
                .code(200)
                .message("Create category successfully")
                .data(createdCategory)
                .build();

    }
}
