package com.simgok.sooper.model;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {
    private Account account;

    public UserAccount(Account account) {
        super(account.getNickName(), account.getPassword(), account.getRole().equals(Role.ADMIN)? List.of(new SimpleGrantedAuthority("ROLE_ADMIN")): List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }
}
