package com.datnguyeni.shop_backend.dto.responseDTO;


import com.datnguyeni.shop_backend.entity.Category;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;

    private List<CategoryResponse> children;
}