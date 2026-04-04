package com.datnguyeni.shop_backend.SpecificationFiter;

import com.datnguyeni.shop_backend.entity.Category;
import com.datnguyeni.shop_backend.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {


    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
        };
    }


    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return criteriaBuilder.conjunction();
            if (minPrice != null && maxPrice != null) return criteriaBuilder.between(root.get("basePrice"), minPrice, maxPrice);
            if (minPrice != null) return criteriaBuilder.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
            return criteriaBuilder.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
        };
    }

    public static Specification<Product> hasCategorySlug(String slug) {
        return (root, query, criteriaBuilder) -> {
            if (slug == null || slug.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            // Cú pháp Join bảng trong JPA - category là tên biến @ManyToOne trong entity Product
            Join<Object, Object> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("slug"), slug);
        };
    }


}
