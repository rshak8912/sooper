package com.simgok.sooper.controllers;

import com.simgok.sooper.model.Category;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.model.form.CategoryForm;
import com.simgok.sooper.model.form.ItemForm;
import com.simgok.sooper.services.AdminService;
import com.simgok.sooper.validators.CategoryFormValidator;
import com.simgok.sooper.validators.ItemFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.io.IOException;

import static com.simgok.sooper.controllers.AdminController.ADMIN;
import static com.simgok.sooper.controllers.AdminController.ROOT;

@Slf4j
@Controller
@RequestMapping(ROOT + ADMIN) // /admin
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ModelMapper modelMapper;
    private final CategoryFormValidator categoryFormValidator;

    private final ItemFormValidator itemFormValidator;
    static final String ROOT = "/";

    static final String ADMIN = "admin";

    static final String ITEMS = "/items";

    static final String CATEGORIES = "/categories";

    static final String INDEX = "/index";

    static final String DELETE = "/delete";

    static final String ADD = "/add";


    @InitBinder("categoryForm")
    public void categoryFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(categoryFormValidator);
    }

    @InitBinder("itemForm")
    public void itemFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(itemFormValidator);
    }
    /*
    * admin/categories
    * */
    @GetMapping(CATEGORIES)
    public String categoryHome(Model model) {
        model.addAttribute("categories", adminService.getAllCategories());
        return ADMIN + CATEGORIES + INDEX;
    }

    /*
    * admin/categories/add
    * */
    @GetMapping(CATEGORIES + ADD)
    public String categoryAdd(Model model) {
        model.addAttribute(new CategoryForm());

        return ADMIN + CATEGORIES + ADD;
    }
    /*
     * admin/items
     * */
    @GetMapping(ITEMS)
    public String itemHome(Model model) {
        model.addAttribute("items", adminService.getAllItems());
        return ADMIN + ITEMS + INDEX;
    }

    /*
     * admin/item/add
     * */
    @GetMapping(ITEMS + ADD)
    public String itemAdd(Model model) {
        model.addAttribute(new ItemForm());
        model.addAttribute("category", adminService.getAllCategories());

        return ADMIN + ITEMS + ADD;
    }
    /*
     * admin/item/add
     * */
    @PostMapping(ITEMS + ADD)
    public String itemCreate(@Valid ItemForm itemForm,
                             Errors errors,
                             @RequestParam("image") MultipartFile file,
                             RedirectAttributes redirectAttributes,
                             Model model) throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute("category", adminService.getAllCategories());
            return ADMIN + ITEMS + ADD;
        }
        else {
            redirectMessage(redirectAttributes, "상품이 추가 되었습니다.", "alert-success");
        }

        if (!adminService.checkFileExtension(file.getOriginalFilename())) {
            redirectMessage(redirectAttributes, "이미지를 선택해주세요. 파일 확장자는 jpg 또는 png 로 선택해야 합니다.", "alert-danger");

        } else {
            Item mappingItem = modelMapper.map(itemForm, Item.class);
            adminService.saveImage(file.getOriginalFilename(), file.getBytes());
            adminService.saveItem(file.getOriginalFilename(), mappingItem);
        }

        return "redirect:" + ROOT+ ADMIN + ITEMS + ADD;
    }


    /*
    * admin/categories/add
    * */
    @PostMapping(CATEGORIES + ADD)
    public String newCategorySubmit(@Valid CategoryForm categoryForm,
                                    Errors errors) {
        if (errors.hasErrors()) {
            return  ADMIN + CATEGORIES + ADD;
        }

        adminService.saveNewCategory(modelMapper.map(categoryForm, Category.class));

        return "redirect:" + ROOT + ADMIN + CATEGORIES;
    }

    /*
    * admin/delete/id
    * */
    @GetMapping(CATEGORIES + DELETE + ROOT + "{id}")
    public String edit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Category category = adminService.getCategoryById(id);

        redirectMessage(redirectAttributes, category.getName()+ " 이(가) 삭제되었습니다.", "alert-success");

        adminService.deleteCategory(id);

        return "redirect:/"+ ADMIN + CATEGORIES;
    }

    private void redirectMessage(RedirectAttributes redirectAttributes, String message, String alert) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("alertClass", alert);
    }



}
