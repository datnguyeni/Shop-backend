package com.datnguyeni.shop_backend.dto.responseDTO;


import com.datnguyeni.shop_backend.entity.Category;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class CategoryResponse {
    private Long id;
    private String name;

}