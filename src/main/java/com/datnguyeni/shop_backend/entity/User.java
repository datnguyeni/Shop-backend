package com.datnguyeni.shop_backend.entity;


import com.datnguyeni.shop_backend.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // Ánh xạ đúng tên bảng trong SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khớp với IDENTITY(1,1) của SQL Server
    private Long id;

    @Column(nullable = false, unique = true, length = 100) // Ánh xạ UNIQUE NOT NULL
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "lastname", length = 50)
    private String lastname;

    @Column(name = "firstname", length = 50)
    private String firstname;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING) // Lưu dưới dạng chuỗi 'ACTIVE' hoặc 'BLOCKED'
    private UserStatus status = UserStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tự động cập nhật thời gian khi có thay đổi
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


}
