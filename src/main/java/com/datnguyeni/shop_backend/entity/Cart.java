package com.datnguyeni.shop_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@Validated
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Long userid;
}
