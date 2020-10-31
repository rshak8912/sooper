package com.simgok.sooper.controllers;

import com.simgok.sooper.model.Account;
import com.simgok.sooper.model.Category;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.model.Order;
import com.simgok.sooper.repositories.CategoryRepository;
import com.simgok.sooper.repositories.ItemRepository;
import com.simgok.sooper.repositories.OrderRepository;
import com.simgok.sooper.services.OrderService;
import com.simgok.sooper.settings.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryRepository categoryRepository;

    private final OrderService orderService;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categoryRepository.findAll());

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
    public String searchItem(@CurrentUser Account account,
                             String keyword, Model model,
                             @PageableDefault(size = 9, sort = "category.name",
                                     direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Item> searchList = orderService.searchResult(keyword, pageable);

        model.addAttribute("searchList", searchList);
        model.addAttribute("searchCount", searchList.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("accountId", account.getId());

        return "search";
    }

}
