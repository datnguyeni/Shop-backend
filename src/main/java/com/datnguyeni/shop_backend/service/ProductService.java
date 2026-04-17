package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.SpecificationFiter.ProductSpecification;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductCreationRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductFilterRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductCreationResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductsResponse;
import com.datnguyeni.shop_backend.entity.Category;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.entity.ProductImage;
import com.datnguyeni.shop_backend.mapper.ProductMapper;
import com.datnguyeni.shop_backend.repository.CategoryRepository;
import com.datnguyeni.shop_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;


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

    @Transactional
    public ProductCreationResponse createProduct(ProductCreationRequest request, List<MultipartFile> files) throws IOException {
        // 1. Tìm Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Category"));

        // 2. Map DTO sang Entity
        Product product = productMapper.toProduct(request);
        product.setCategory(category);

        // 3. XỬ LÝ UPLOAD ẢNH DÙNG BUILDER
        if (files != null && !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);

                // Đẩy ảnh lên Cloudinary
                String url = cloudinaryService.uploadImage(file);

                ProductImage productImage =  new ProductImage();
                productImage.setProduct(product); // gan cha cho con
                productImage.setImageUrl(url);
                if (i == 0) {
                    productImage.setDefaultValue(true);
                } else {
                    productImage.setDefaultValue(false);
                }


                // Add vào danh sách (Đảm bảo class Product đã có: private Set<ProductImage> images = new HashSet<>();)
                product.getImages().add(productImage);
            }
        }

        // 4. Lưu một phát ăn ngay (nhờ CascadeType.ALL)
        Product createdProduct =  productRepository.save(product);
        return productMapper.toProductCreationResponse(createdProduct);
    }

    


}