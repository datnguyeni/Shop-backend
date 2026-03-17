package com.datnguyeni.shop_backend.mapper;

import com.datnguyeni.shop_backend.dto.responseDTO.ProductImageResponse;
import com.datnguyeni.shop_backend.entity.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    ProductImageResponse toImageResponse(ProductImage productImage);

}
