package com.datnguyeni.shop_backend.mapper;

import com.datnguyeni.shop_backend.dto.responseDTO.ProductImageResponse;
import com.datnguyeni.shop_backend.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(source = "defaultValue", target = "defaultValue")
    ProductImageResponse toImageResponse(ProductImage productImage);


}
