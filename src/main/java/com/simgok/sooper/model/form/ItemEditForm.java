package com.simgok.sooper.model.form;

import com.simgok.sooper.model.Category;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ItemEditForm {

    private String name;

    @Min(0)
    private Integer price;

    @Min(0)
    private Integer stockQuantity;

    private Category category = new Category();

    // TODO path는 변경 전 상품이름으로 사용 중
    private String path;

}
