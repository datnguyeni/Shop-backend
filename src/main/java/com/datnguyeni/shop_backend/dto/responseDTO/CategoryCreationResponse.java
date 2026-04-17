package com.datnguyeni.shop_backend.dto.responseDTO;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreationResponse {

    private String name;
    private Long parent_id;
    private String slug;
    private String description;

}
