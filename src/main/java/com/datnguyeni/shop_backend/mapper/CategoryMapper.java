package com.datnguyeni.shop_backend.mapper;


import com.datnguyeni.shop_backend.dto.responseDTO.CategoryResponse;
import com.datnguyeni.shop_backend.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);

}
