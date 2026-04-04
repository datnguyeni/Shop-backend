package com.datnguyeni.shop_backend.dto.requestDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreationRequest {
    private String name;
    private BigDecimal basePrice;
    private String description;
    private Long categoryId;

    private List<VariantRequest> variants;

    private List<ImageRequest> images;

    @Data
    public static class VariantRequest {
        private String size;
        private String color;
        private Integer stockQuantity;
        private String sku;
    }

    @Data
    public static class ImageRequest {
        private String imageUrl;
        private Boolean isDefault;
    }
}