package com.datnguyeni.shop_backend.SpecificationFiter;

import com.datnguyeni.shop_backend.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    public static Specification<Category> byParentId(Long parentId) {
        return (root, query, cb) -> {
            // Trường hợp 1: Nếu người dùng muốn tìm Danh mục gốc (parentId = null)
            // Phải dùng hàm isNull() của SQL
            if (parentId == null) {
                return cb.isNull(root.get("parent"));
            }

            // Trường hợp 2: Tìm danh mục con theo ID của cha
            // Phải chui vào "parent", rồi chui tiếp vào "id" để lấy số ra so sánh
            return cb.equal(root.get("parent").get("id"), parentId);
        };
    }
}