package com.simgok.sooper.model.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CategoryForm {
    @NotBlank
    @Length(min = 1, max = 20)
    private String name;

}
