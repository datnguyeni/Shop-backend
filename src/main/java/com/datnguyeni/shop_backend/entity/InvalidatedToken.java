package com.datnguyeni.shop_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invalidated_token")
public class InvalidatedToken {
// jti is unique -> can't mess with other user

    @Id
    private String id;
    private Date expiryTime;
}
