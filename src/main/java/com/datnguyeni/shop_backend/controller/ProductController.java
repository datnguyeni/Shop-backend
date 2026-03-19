package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.PagingRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductFilterRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductsResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.repository.ProductRepository;
import com.datnguyeni.shop_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("get-all")
    public ApiResponse<Page<ProductsResponse>> getAllProducts(PagingRequest pagingRequest) {

        Pageable pageable = pagingRequest.getPageable();
        Page<ProductsResponse> productsResponse = productService.getAllProducts(pageable);

        return ApiResponse.<Page<ProductsResponse>>builder()
                    .code(200)
                    .message("Get all products successfully")
                    .data(productsResponse)
                    .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponse> getProductById(@PathVariable Long id) {

    ProductDetailResponse productDetailResponse = productService.getProductDetail(id);

    return ApiResponse.<ProductDetailResponse>builder()
            .code(200)
            .message("Get product successfully")
            .data(productDetailResponse)
            .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<ProductsResponse>> searchProducts(ProductFilterRequest request) {

        Pageable pageable = request.getPageableWithSort();

        Page<ProductsResponse> productsResponses = productService.searchProducts(pageable, request);

        return ApiResponse.<Page<ProductsResponse>>builder()
                .code(200)
                .message("Search and sort products successfully")
                .data(productsResponses)
                .build();
    }



}
