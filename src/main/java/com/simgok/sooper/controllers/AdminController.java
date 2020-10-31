package com.simgok.sooper.controllers;

import com.simgok.sooper.model.Category;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.model.form.CategoryForm;
import com.simgok.sooper.model.form.ItemEditForm;
import com.simgok.sooper.model.form.ItemForm;
import com.simgok.sooper.repositories.ItemRepository;
import com.simgok.sooper.services.AdminService;
import com.simgok.sooper.validators.CategoryFormValidator;
import com.simgok.sooper.validators.ItemEditFormValidator;
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
    private final ItemRepository itemRepository;
    private final AdminService adminService;
    private final ModelMapper modelMapper;
    private final CategoryFormValidator categoryFormValidator;

    private final ItemFormValidator itemFormValidator;

    private final ItemEditFormValidator itemEditFormValidator;


    static final String ROOT = "/";

    static final String ADMIN = "admin";

    static final String ITEMS = "/items";

    static final String CATEGORIES = "/categories";

    static final String INDEX = "/index";

    static final String DELETE = "/delete";

    static final String ADD = "/add";

    static final String EDIT = "/edit";

    @InitBinder("categoryForm")
    public void categoryFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(categoryFormValidator);
    }

    @InitBinder("itemForm")
    public void itemFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(itemFormValidator);
    }

    @InitBinder("itemEditForm")
    public void itemEditFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(itemEditFormValidator);
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
     * admin/categories/delete/id
     * */
    @GetMapping(CATEGORIES + DELETE + ROOT + "{id}")
    public String edit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Category category = adminService.getCategoryById(id);

        redirectMessage(redirectAttributes, category.getName()+ " 이(가) 삭제되었습니다.", "alert-success");

        adminService.deleteCategory(id);

        return "redirect:/"+ ADMIN + CATEGORIES;
    }
    /*
    *  admin/items/delete/id
    * */
    @GetMapping(ITEMS + DELETE + ROOT + "{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Item item = adminService.getItemById(id);

        redirectMessage(redirectAttributes, item.getName()+ " 이(가) 삭제되었습니다.", "alert-success");

        adminService.deleteItem(id);

        return "redirect:/"+ ADMIN + ITEMS;
    }
    @GetMapping(ITEMS + EDIT + ROOT + "{id}")
    public String editItem(@PathVariable Long id, Model model) {
        Item item = adminService.getEditItem(id);
        model.addAttribute(modelMapper.map(item, ItemEditForm.class));
        model.addAttribute("itemName", item.getName());
        model.addAttribute("categories", adminService.getAllCategories());
        return ADMIN + ITEMS + EDIT;
    }

    @PostMapping(ITEMS + EDIT)
    public String editSubmit(@Valid ItemEditForm itemEditForm,
                             Errors errors,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes,
                             Model model) throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute("category", adminService.getAllCategories());
            model.addAttribute("itemName", itemEditForm.getName());
            return ADMIN + ITEMS + EDIT;
        }
        else {
            redirectMessage(redirectAttributes, "상품이 수정 되었습니다.", "alert-success");
        }

        Long originalItemId = adminService.getItemIdByName(itemEditForm.getPath());
        Item originalItem = adminService.getItemById(originalItemId);

        if (!adminService.checkFileExtension(file.getOriginalFilename())) {
            redirectMessage(redirectAttributes, "이미지를 선택해주세요. 파일 확장자는 jpg 또는 png 로 선택해야 합니다.", "alert-danger");

        } else {

            adminService.saveImage(file.getOriginalFilename(), file.getBytes());
            editItemMapping(itemEditForm, file, originalItem);
            model.addAttribute("items", adminService.getAllItems());
        }

        return "redirect:" + ROOT+ ADMIN + ITEMS + EDIT + ROOT + originalItemId;
    }

    private void editItemMapping(ItemEditForm itemEditForm, MultipartFile file, Item originalItem) {
        originalItem.setName(itemEditForm.getName());
        originalItem.setCategory(itemEditForm.getCategory());
        originalItem.setPrice(itemEditForm.getPrice());
        originalItem.setStockQuantity(itemEditForm.getStockQuantity());
        originalItem.setImage(file.getOriginalFilename());
        originalItem.setPath(originalItem.getName());
    }


    private void redirectMessage(RedirectAttributes redirectAttributes, String message, String alert) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("alertClass", alert);
    }



}