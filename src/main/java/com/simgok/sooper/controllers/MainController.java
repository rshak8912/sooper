package com.simgok.sooper.controllers;

import com.simgok.sooper.model.Account;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.repositories.ItemRepository;
import com.simgok.sooper.settings.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemRepository itemRepository;


    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/search")
    public String searchItem(@CurrentUser Account account, String keyword, Model model) {
        List<Item> searchList = itemRepository.findByKeyword(keyword);

        model.addAttribute("searchList", searchList);
        model.addAttribute("searchCount", searchList.size());
        model.addAttribute("keyword", keyword);
        model.addAttribute("accountId", account.getId());
        return "search";
    }



}
