package com.simgok.sooper.model.form;

import com.simgok.sooper.model.Category;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.EAGER;

@Data
public class ItemForm {
    @NotBlank
    @Length(min = 1, max = 30)
    private String name;

    @Min(0)
    private Integer price;

    @Min(0)
    private Integer stockQuantity;

    private Category category = new Category();

    @NotBlank
    @Length(min = 1, max = 30)
    private String path;

}
