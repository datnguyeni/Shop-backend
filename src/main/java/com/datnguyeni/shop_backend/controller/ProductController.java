package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.PagingRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.repository.ProductRepository;
import com.datnguyeni.shop_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get-all")
    public ApiResponse<Page<ProductDetailResponse>> getAllProducts(PagingRequest pagingRequest) {

        Pageable pageable = pagingRequest.getPageable();

        Page<ProductDetailResponse> productDetailResponsePage = productService.getProducts(pageable);

        return ApiResponse.<Page<ProductDetailResponse>>builder()
                .code(200)
                .message("Get all products successfully")
                .data(productDetailResponsePage)
                .build();
    }




}
