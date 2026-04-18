package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.ProductVariantCreationRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductVariantCreationResponse;
import com.datnguyeni.shop_backend.entity.ProductVariant;
import com.datnguyeni.shop_backend.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/productVariant")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductVariantCreationResponse> createProductVariant(
            @RequestPart("productVariant")ProductVariantCreationRequest productVariantCreationRequest,
            @RequestPart("files") List<MultipartFile> files) throws IOException {

        ProductVariantCreationResponse createdProduct = productVariantService.createProductVariant(productVariantCreationRequest, files);

        return ApiResponse.<ProductVariantCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Product Variant created successfully")
                .data(createdProduct)
                .build();

    }


}
