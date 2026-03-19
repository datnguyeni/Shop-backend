package com.datnguyeni.shop_backend.dto.requestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest extends PagingRequest {

    private String productName;
    private Double price;
    private Double minPrice;
    private Double maxPrice;
    private String productSize;

    private Long categoryParentId;

}
