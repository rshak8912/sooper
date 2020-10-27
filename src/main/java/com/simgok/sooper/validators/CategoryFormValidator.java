package com.simgok.sooper.validators;

import com.simgok.sooper.model.Category;
import com.simgok.sooper.model.form.CategoryForm;
import com.simgok.sooper.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CategoryFormValidator implements Validator {
    private final CategoryRepository categoryRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryForm categoryForm = (CategoryForm)target;
        if (categoryRepository.existsByName(categoryForm.getName())) {
            errors.rejectValue("name", "wrong.name", "해당 카테고리 이름을 사용할 수 없습니다.");
        }
    }
}
