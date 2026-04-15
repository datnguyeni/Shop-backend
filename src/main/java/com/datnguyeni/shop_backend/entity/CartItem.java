package com.datnguyeni.shop_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Validated
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Transient
    public BigDecimal getSubTotal() {
        if (this.quantity == null || this.variant == null ||
                this.variant.getProduct() == null || this.variant.getProduct().getBasePrice() == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(this.quantity)
                .multiply(this.variant.getProduct().getBasePrice());
    }

}


