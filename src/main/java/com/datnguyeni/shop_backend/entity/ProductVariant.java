package com.datnguyeni.shop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_variants") // Ánh xạ đúng tên bảng trong SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private String color;
    private Long stockQuantity;
    private String sku;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
