package com.simgok.sooper.controllers;

import com.simgok.sooper.model.Account;
import com.simgok.sooper.model.Profile;
import com.simgok.sooper.services.AccountService;
import com.simgok.sooper.settings.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    static final String SETTINGS_PROFILE_URL = "/settings/profile";
    static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @GetMapping(SETTINGS_PROFILE_URL)
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS_PROFILE_VIEW_NAME;
    }

    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }
        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message", "프로필이 수정되었습니다.");
        return "redirect:" + SETTINGS_PROFILE_URL;
    }
}
