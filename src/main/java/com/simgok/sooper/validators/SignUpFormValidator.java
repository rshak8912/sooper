package com.simgok.sooper.validators;


import com.simgok.sooper.model.SignUpForm;
import com.simgok.sooper.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)object;
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if(accountRepository.existsByNickName("관리자") || accountRepository.existsByNickName("admin")) {
            errors.rejectValue("nickName", "invalid.nickName", new Object[]{signUpForm.getNickName()}, "사용 불가능한 닉네임입니다.");
        }
    }
}
