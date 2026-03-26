package com.datnguyeni.shop_backend.service;


import com.datnguyeni.shop_backend.SpecificationFiter.ProductSpecification;
import com.datnguyeni.shop_backend.dto.requestDTO.PagingRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductFilterRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductsResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.mapper.ProductMapper;
import com.datnguyeni.shop_backend.repository.ProductRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository,  ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }


    public Page<ProductsResponse> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(productMapper::toProductsResponse);
    }



    public ProductDetailResponse getProductDetail(Long id) {

        return productRepository.findById(id)
                .map(productMapper::toProductDetailResponse)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public Page<ProductsResponse> searchProducts(Pageable pageable, ProductFilterRequest productFilterRequest) {

        // Kết hợp TẤT CẢ các điều kiện lại với nhau: Vừa lọc Giá, vừa lọc Danh mục
        Specification<Product> spec = Specification.where(ProductSpecification.hasPriceBetween(productFilterRequest.getMinPrice(), productFilterRequest.getMaxPrice()))
                .and(ProductSpecification.hasCategoryId(productFilterRequest.getCategoryId()));

        return productRepository.findAll(spec, pageable)
                .map(productMapper::toProductsResponse);
    }


}
