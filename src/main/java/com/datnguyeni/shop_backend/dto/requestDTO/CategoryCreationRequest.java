package com.datnguyeni.shop_backend.dto.requestDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreationRequest {

    private String name;
    private Long parent_id;
    private String slug;
    private String description;

}
