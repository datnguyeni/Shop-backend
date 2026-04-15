package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.SpecificationFiter.ProductSpecification;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductFilterRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductsResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.mapper.ProductMapper;
import com.datnguyeni.shop_backend.repository.CategoryRepository;
import com.datnguyeni.shop_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    public Page<ProductsResponse> getProductsByCategorySlug(String slug, Pageable pageable) {
        return productRepository.findByCategory_Slug(slug, pageable)
                .map(productMapper::toProductsResponse);
    }


    //  Search/Filter
    public Page<ProductsResponse> getFilteredProducts(ProductFilterRequest filter, Pageable pageable) {

        Specification<Product> spec = Specification.where(
                        ProductSpecification.hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice())
                )
                .and(ProductSpecification.hasKeyword(filter.getKeyword()))          // Lấy keyword từ DTO
                .and(ProductSpecification.hasCategorySlug(filter.getCategorySlug())); // Lấy slug từ DTO


        return productRepository.findAll(spec, pageable)
                .map(productMapper::toProductsResponse);
    }

    public ProductDetailResponse getProductDetail(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductDetailResponse)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public List<ProductsResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(productMapper::toProductsResponse)
                .toList();
    }

}