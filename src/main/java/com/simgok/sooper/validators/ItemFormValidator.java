package com.simgok.sooper.validators;

import com.simgok.sooper.model.form.ItemForm;
import com.simgok.sooper.repositories.ItemRepository;
import com.simgok.sooper.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ItemFormValidator implements Validator {
    private final ItemRepository itemRepository;
    private final AdminService adminService;
    @Override
    public boolean supports(Class<?> clazz) {
        return ItemForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemForm itemForm = (ItemForm)target;
        if (itemRepository.existsByName(itemForm.getName())) {
            errors.rejectValue("name","wrong.name", "해당 상품 이름을 사용할 수 없습니다.");
        }
        if (itemForm.getPrice() < 0) {
            errors.rejectValue("price","wrong.price","상품의 가격은 0원 이상입니다.");
        }
        if (itemForm.getStockQuantity() < 0) {
            errors.rejectValue("stockQuantity", "wrong.stockQuantity", "상품의 수량은 0개 이상입니다.");
        }
        if (itemRepository.existsByPath(itemForm.getPath())) {
            errors.rejectValue("path","wrong.path", "해당 상품 경로를 사용할 수 없습니다.");
        }

    }
}
