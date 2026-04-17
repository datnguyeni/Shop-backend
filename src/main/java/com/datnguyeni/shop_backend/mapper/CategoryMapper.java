package com.datnguyeni.shop_backend.mapper;


import com.datnguyeni.shop_backend.dto.requestDTO.CategoryCreationRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.CategoryCreationResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.CategoryResponse;
import com.datnguyeni.shop_backend.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);

    @Mapping(source = "parent.id", target = "parent_id") // Bảo MapStruct lấy ID của cha gán vào parent_id
    CategoryCreationResponse toCategoryCreationResponse(Category Category);

    Category toCategory(CategoryCreationRequest request);




}
