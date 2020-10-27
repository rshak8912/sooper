package com.simgok.sooper.controllers;

import com.simgok.sooper.infra.MockMvcTest;
import com.simgok.sooper.model.form.SignUpForm;
import com.simgok.sooper.repositories.AccountRepository;
import com.simgok.sooper.services.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class MainControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void createAccount() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setDetailsLocation("123-45번지");
        signUpForm.setEmail("rshak123@gmail.com");
        signUpForm.setLocation("분당구 야탑동");
        signUpForm.setNickname("홍길동");
        signUpForm.setPassword("123123123");

        accountService.processNewAccount(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로 로그인 - 성공")
    @Test
    void login_with_email() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "rshak123@gmail.com")
                .param("password", "123123123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("홍길동"));
    }

    @DisplayName("이름으로 로그인 -성공")
    @Test
    void login_with_name() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "홍길동")
                .param("password", "123123123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andExpect(authenticated().withUsername("홍길동"));
    }

    @DisplayName("로그인 - 실패")
    @Test
    void login_fail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "11")
                .param("password", "222")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}