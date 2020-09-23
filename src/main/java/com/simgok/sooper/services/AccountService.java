package com.simgok.sooper.services;

import com.simgok.sooper.model.*;
import com.simgok.sooper.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public Account processNewAccount(SignUpForm signUpForm) {
        return saveNewAccount(signUpForm);
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Role role = Role.USER;
        if (signUpForm.getNickName().equals("admin")) {
            role=Role.ADMIN;
        }
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickName(signUpForm.getNickName())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .role(role)
                .location(signUpForm.getLocation())
                .detailsLocation(signUpForm.getDetailsLocation())
                .build();

        return accountRepository.save(account);
    }

    public void login(Account account) {
        if (account.getNickName().equals("관리자") || account.getNickName().equals("admin")) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    new UserAccount(account),
                    account.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
            SecurityContextHolder.getContext().setAuthentication(token);
        } else {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    new UserAccount(account),
                    account.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrName) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrName);
        if (account == null) {
            account = accountRepository.findByNickName(emailOrName);
        }
        if (account == null) {
            throw new UsernameNotFoundException(emailOrName);
        }
        return new UserAccount(account);
    }

    public void updateProfile(Account account, Profile profile) {
        modelMapper.map(profile, account);

        accountRepository.save(account);
    }
}
