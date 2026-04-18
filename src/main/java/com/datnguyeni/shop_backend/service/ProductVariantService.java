package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.dto.requestDTO.ProductVariantCreationRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductVariantCreationResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.entity.ProductImage;
import com.datnguyeni.shop_backend.entity.ProductVariant;
import com.datnguyeni.shop_backend.mapper.ProductVariantMapper;
import com.datnguyeni.shop_backend.repository.ProductRepository;
import com.datnguyeni.shop_backend.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;
    private final CloudinaryService cloudinaryService;
    private final ProductRepository productRepository;


    public ProductVariantCreationResponse createProductVariant(ProductVariantCreationRequest request,
                                                               List<MultipartFile> files) throws IOException {

        // 1. Map các dữ liệu cơ bản từ Request sang Entity
        ProductVariant productVariant = productVariantMapper.toProductVariant(request);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Product với ID: " + request.getProductId()));

        productVariant.setProduct(product); // Quan trọng 1: Gán cha cho Variant

        // 3. Xử lý ảnh
        if (files != null && !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);

                // Đẩy ảnh lên Cloudinary
                String url = cloudinaryService.uploadImage(file);

                ProductImage productImage = new ProductImage();

                // GÁN CẢ 2 ID ĐỂ DATABASE KHÔNG BÁO LỖI NULL
                productImage.setVariant(productVariant); // Quan trọng 2: Gán Variant_id
                productImage.setProduct(product);        // Quan trọng 3: Gán Product_id (Thiếu cái này sinh lỗi 500)

                productImage.setImageUrl(url);
                productImage.setColor(request.getColor());

                if (i == 0) {
                    productImage.setDefaultValue(true);
                } else {
                    productImage.setDefaultValue(false);
                }

                productVariant.getImages().add(productImage);
            }
        }

        // 4. Lưu một phát ăn ngay (nhờ CascadeType.ALL)
        ProductVariant createdProductVariant = productVariantRepository.save(productVariant);
        return productVariantMapper.toProductVariantCreationResponse(createdProductVariant);
    }


}
