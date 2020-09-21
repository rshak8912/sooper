package com.simgok.sooper.controllers;

import com.simgok.sooper.model.Account;
import com.simgok.sooper.model.SignUpForm;
import com.simgok.sooper.repositories.AccountRepository;
import com.simgok.sooper.services.AccountService;
import com.simgok.sooper.settings.CurrentUser;
import com.simgok.sooper.validators.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;

    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";
    }

    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable Long id, Model model, @CurrentUser Account account) {
        Account byId = accountRepository.readById(id);
        if (byId == null) {
            new IllegalArgumentException("존재하지 않는 회원입니다.");
        } else {
            String name = byId.getName();
            model.addAttribute("account", byId);
            model.addAttribute("isAdmin", (name.equals("관리자") || name.equals("admin")));
            model.addAttribute("isOwner", byId.equals(account));
        }

        return "account/profile";
    }
}
