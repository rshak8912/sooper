package com.simgok.sooper.validators;

import com.simgok.sooper.model.form.ItemEditForm;
import com.simgok.sooper.model.form.ItemForm;
import com.simgok.sooper.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ItemEditFormValidator implements Validator {
    private final ItemRepository itemRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return ItemEditForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemEditForm itemEditForm = (ItemEditForm)target;
        if (itemRepository.existsByName(itemEditForm.getPath()) == false) {
            if (itemRepository.existsByName(itemEditForm.getName())) {
                errors.rejectValue("name","wrong.name", "해당 상품 이름을 사용할 수 없습니다.");
            }
        }
        if (itemEditForm.getPrice() < 0) {
            errors.rejectValue("price","wrong.price","상품의 가격은 0원 이상입니다.");
        }
        if (itemEditForm.getStockQuantity() < 0) {
            errors.rejectValue("stockQuantity", "wrong.stockQuantity", "상품의 수량은 0개 이상입니다.");
        }

    }
}
