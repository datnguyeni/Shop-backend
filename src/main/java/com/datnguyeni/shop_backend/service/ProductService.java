package com.datnguyeni.shop_backend.service;


import com.datnguyeni.shop_backend.dto.requestDTO.PagingRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.mapper.ProductMapper;
import com.datnguyeni.shop_backend.repository.ProductRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    public Page<ProductDetailResponse> getProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(productMapper::toProductDetailResponse);
    }


}
