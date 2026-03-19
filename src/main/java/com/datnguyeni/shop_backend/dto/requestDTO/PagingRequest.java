package com.datnguyeni.shop_backend.dto.requestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingRequest {

    private Integer page;
    private Integer size;

    private String sortBy = "id"; // Mặc định sắp xếp theo id
    private Boolean isAsc = false; // Mặc định là giảm dần (mới nhất lên đầu)

    public Pageable getPageable() {
        // Trả về đối tượng Pageable để dùng trong Repository
        // Syntax: PageRequest.of(trang_số, số_phần_tử)
        return  PageRequest.of(this.page, this.size);
    }


    public Pageable getPageableWithSort() {
        Sort sort = isAsc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return (Pageable) PageRequest.of(this.page, this.size, sort);
    }


}
