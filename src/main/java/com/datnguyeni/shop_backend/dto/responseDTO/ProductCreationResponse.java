package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreationResponse {

    private String name;
    private BigDecimal basePrice;
    private String description;
    private Long categoryId;

    private List<ProductImageResponse> images;

}

