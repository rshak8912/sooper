package com.simgok.sooper.services;

import com.simgok.sooper.model.Account;
import com.simgok.sooper.model.Role;
import com.simgok.sooper.model.SignUpForm;
import com.simgok.sooper.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account processNewAccount(SignUpForm signUpForm) {
        return saveNewAccount(signUpForm);
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Role role = Role.USER;
        if (signUpForm.getName().equals("admin")) {
            role=Role.ADMIN;
        }
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .name(signUpForm.getName())
                .password(passwordEncoder.encode(signUpForm.getPassword())) // TODO encoding 해야함
                .role(role)
                .location(signUpForm.getLocation())
                .detailsLocation(signUpForm.getDetailsLocation())
                .build();

        return accountRepository.save(account);
    }

    public void login(Account account) {
        if (account.getName().equals("관리자") || account.getName().equals("admin")) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    account.getName(),
                    account.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
            SecurityContextHolder.getContext().setAuthentication(token);
        } else {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    account.getName(),
                    account.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }
}
