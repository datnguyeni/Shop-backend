package com.datnguyeni.shop_backend.SpecificationFiter;

import com.datnguyeni.shop_backend.entity.Category;
import com.datnguyeni.shop_backend.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasPriceBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("basePrice"), min, max);
            if (min != null) return cb.greaterThanOrEqualTo(root.get("basePrice"), min);
            return cb.lessThanOrEqualTo(root.get("basePrice"), max);
        };
    }


    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;

            // JOIN từ Product sang Category
            Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);

            // Điều kiện 1: Sản phẩm thuộc đúng Danh mục đó (Ví dụ: Áo sơ mi)
            Predicate directMatch = cb.equal(categoryJoin.get("id"), categoryId);

            // Điều kiện 2: Sản phẩm thuộc Danh mục con của Danh mục đó (Ví dụ: Áo Nam)
            Predicate parentMatch = cb.equal(categoryJoin.get("parent").get("id"), categoryId);

            // Gộp 2 điều kiện bằng toán tử OR
            return cb.or(directMatch, parentMatch);
        };
    }



}
