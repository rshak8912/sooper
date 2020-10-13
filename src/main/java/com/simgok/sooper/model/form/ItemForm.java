package com.simgok.sooper.model.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ItemForm {
    @NotBlank
    @Length(min = 1, max = 10)
    private String name;

    @NotBlank
    private Integer price;

    @NotBlank
    private Integer stockQuantity;

}
