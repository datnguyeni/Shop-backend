package com.datnguyeni.shop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


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
    @Column(name = "stock_quantity")
    private Long stockQuantity;
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @Transient
    public String getDefaultImageUrl() {
        if (this.images == null || this.images.isEmpty()) {
            return null;
        }

        for (ProductImage img : this.images) {
            if (img.isDefaultValue()) {
                return img.getImageUrl();
            }
        }

        return this.images.iterator().next().getImageUrl();
    }


}
