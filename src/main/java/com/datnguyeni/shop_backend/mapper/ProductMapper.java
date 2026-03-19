package com.datnguyeni.shop_backend.mapper;


import com.datnguyeni.shop_backend.dto.responseDTO.ProductDetailResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductVariantResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.ProductsResponse;
import com.datnguyeni.shop_backend.entity.Category;
import com.datnguyeni.shop_backend.entity.Product;
import com.datnguyeni.shop_backend.entity.ProductImage;
import com.datnguyeni.shop_backend.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring", uses = {
        CategoryMapper.class,
        ProductImageMapper.class,
        ProductVariantMapper.class
})
public interface ProductMapper {

//    @Mappings({
//            @Mapping(source = "product.description", target = "description"),
//            @Mapping(source = "product.basePrice", target = "basePrice"),
//            @Mapping(source = "product.name", target = "productName"),
//            @Mapping(source = "product.id", target = "id"),
//            @Mapping(source = "category.name", target = "categoryName"),
//            @Mapping(source = "productImage.imageUrl", target = "imageUrl"),
//            @Mapping(source = "productImage.isDefault", target = "isDefault"),
//
//    })


    ProductDetailResponse toProductDetailResponse(Product product);

//    @Mapping(source = "category.id", target = "categoryId")
//    @Mapping(source = "category.name", target = "categoryName")
//    @Mapping(source = "category.parent.id", target = "categoryParentId")
    ProductsResponse toProductsResponse(Product product);


}
