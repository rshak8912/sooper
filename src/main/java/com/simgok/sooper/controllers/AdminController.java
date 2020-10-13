package com.simgok.sooper.controllers;

import com.simgok.sooper.model.form.CategoryForm;
import com.simgok.sooper.model.form.ItemForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.simgok.sooper.controllers.AdminController.ADMIN;
import static com.simgok.sooper.controllers.AdminController.ROOT;

@Controller
@RequestMapping(ROOT + ADMIN) // /admin
public class AdminController {
    static final String ROOT = "/";

    static final String ADMIN = "admin";

    static final String ITEMS = "/items";

    static final String CATEGORIES = "/categories";

    static final String INDEX = "/index";

    static final String ADD = "/add";
    @GetMapping(CATEGORIES + INDEX) //admin+category/index
    public String categoryHome(Model model) {

        return ADMIN + CATEGORIES + INDEX;
    }
    @GetMapping(CATEGORIES + ADD)
    public String categoryAdd(Model model) {
        model.addAttribute(new CategoryForm());
        return ADMIN + CATEGORIES + ADD;
    }
}
