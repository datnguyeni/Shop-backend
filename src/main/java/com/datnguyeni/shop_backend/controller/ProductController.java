package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.PagingRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductCreationRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductFilterRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductCreationResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductsResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. Lấy tất cả hoặc lọc sản phẩm
    @GetMapping("/filter")
    public ApiResponse<Page<ProductsResponse>> getProducts(
            ProductFilterRequest filterRequest,
             PagingRequest pagingRequest) {

        Page<ProductsResponse> response = productService.getFilteredProducts(filterRequest, pagingRequest.getPageableWithSort());

        return ApiResponse.<Page<ProductsResponse>>builder()
                .code(200)
                .message("Get products successfully")
                .data(response)
                .build();
    }

//    // 2. Lấy sản phẩm theo Category Slug
//    @GetMapping("/{slug}")
//    public ApiResponse<Page<ProductsResponse>> getProductsByCategory(
//            @PathVariable String slug,
//            PagingRequest pagingRequest) {
//
//        Page<ProductsResponse> response = productService.getProductsByCategorySlug(slug, pagingRequest.getPageable());
//
//        return ApiResponse.<Page<ProductsResponse>>builder()
//                .code(200)
//                .message("Get products by category successfully")
//                .data(response)
//                .build();
//    }

    // 3. Chi tiết sản phẩm
    @GetMapping("/detail/{id}")
    public ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable Long id) {
        ProductDetailResponse response = productService.getProductDetail(id);

        return ApiResponse.<ProductDetailResponse>builder()
                .code(200)
                .message("Get product detail successfully")
                .data(response)
                .build();
    }


    @GetMapping
    public ApiResponse<List<ProductsResponse>> getAllProducts() {
        List<ProductsResponse> response = productService.getAllProducts();

        return ApiResponse.<List<ProductsResponse>>builder()
                .code(200)
                .message("Get all products successfully")
                .data(response)
                .build();

    }


    // ADMIN FEAT

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductCreationResponse> createProduct(
            @RequestPart("product") @Valid ProductCreationRequest productCreationRequest,
            @RequestPart("files") List<MultipartFile> files) throws IOException {

        ProductCreationResponse createdProduct = productService.createProduct(productCreationRequest, files);

        return ApiResponse.<ProductCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create product successfully")
                .data(createdProduct)
                .build();
    }

}