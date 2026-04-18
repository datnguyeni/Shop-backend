package com.datnguyeni.shop_backend.mapper;

import com.datnguyeni.shop_backend.dto.requestDTO.ProductCreationRequest;
import com.datnguyeni.shop_backend.dto.requestDTO.ProductVariantCreationRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductVariantCreationResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductVariantResponse;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.entity.ProductImage;
import com.datnguyeni.shop_backend.entity.ProductVariant;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    ProductVariantResponse toVariantResponse(ProductVariant productVariant);


    ProductVariant toProductVariant(ProductVariantCreationRequest productVariantCreationRequest);
    @AfterMapping
    default void linkChildrenToParent(@MappingTarget ProductVariant productVariant) {

        // Gán reference productVariant cha cho từng image con
        if (productVariant.getImages() != null) {
            productVariant.getImages().forEach(image -> image.setVariant(productVariant));
        }
    }

    ProductVariantCreationResponse toProductVariantCreationResponse(ProductVariant productVariant);


}
