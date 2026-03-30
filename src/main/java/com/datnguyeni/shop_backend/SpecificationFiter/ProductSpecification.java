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


    public static Specification<Product> hasNameLike(String name) {

        return (root, query, cb) -> cb.like(root.get("name"), name + "%");
    }



}
