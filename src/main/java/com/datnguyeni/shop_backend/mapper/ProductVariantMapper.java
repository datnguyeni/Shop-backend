package com.datnguyeni.shop_backend.mapper;

import com.datnguyeni.shop_backend.dto.responseDTO.ProductVariantResponse;
import com.datnguyeni.shop_backend.entity.ProductVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    ProductVariantResponse toVariantResponse(ProductVariant productVariant);
}
