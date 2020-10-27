package com.simgok.sooper.controllers;

import com.simgok.sooper.infra.MockMvcTest;
import com.simgok.sooper.WithAccount;
import com.simgok.sooper.model.Category;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.model.Role;
import com.simgok.sooper.repositories.CategoryRepository;
import com.simgok.sooper.repositories.ItemRepository;
import com.simgok.sooper.services.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AdminService adminService;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemRepository itemRepository;


    @DisplayName("카테고리 화면 출력(관리자) - 성공")
    @WithAccount("admin")
    @Test
    void categoryHome() throws Exception{
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/categories/index"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(authenticated().withRoles(Role.ADMIN.getRole()));
    }

    @DisplayName("카테고리 화면 출력(접근 권한 없는 이용자) - 실패")
    @WithAccount("hahaha")
    @Test
    void categoryAccessForbidden() throws Exception{
        mockMvc.perform(get("/admin/categories/index"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("카테고리 추가 - 성공")
    @WithAccount("admin")
    @Test
    void addCategory() throws Exception {
        mockMvc.perform(post("/admin/categories/add")
                .param("name","라면")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        assertThat(categoryRepository.count()).isEqualTo(1);
    }

    @DisplayName("카테고리 추가 - 실패(긴 이름)")
    @WithAccount("admin")
    @Test
    void addCategory_fail_longname() throws Exception {

        mockMvc.perform(post("/admin/categories/add")
                .param("name","매우 긴 이름의 카테고리123123123123123123")
                .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk());

        assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @DisplayName("카테고리 추가 - 실패(중복 카테고리)")
    @WithAccount("admin")
    @Test
    void addCategory_fail() throws Exception {
        Category category = Category.builder()
                .name("라면")
                .build();

        adminService.saveNewCategory(category);

        mockMvc.perform(post("/admin/categories/add")
                .param("name","라면")
                .with(csrf()))
                .andExpect(model().attributeHasFieldErrors("categoryForm", "name"))
                .andExpect(model().attributeHasFieldErrorCode("categoryForm","name","wrong.name"))
                .andExpect(status().isOk());

        assertThat(categoryRepository.count()).isEqualTo(1);
    }

    @DisplayName("카테고리 삭제")
    @WithAccount("admin")
    @Test
    void deleteCategory_fail() throws Exception {
        Category category = Category.builder()
                .name("라면")
                .build();

        adminService.saveNewCategory(category);

        Optional<Category> byId = categoryRepository.findById(category.getId());
        Long deleteItemId = byId.orElseGet(Category::new).getId();

        mockMvc.perform(get("/admin/categories/delete/" + deleteItemId.toString())
                .with(csrf()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(status().is3xxRedirection());

        assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @DisplayName("아이템 화면 출력(관리자) - 성공")
    @WithAccount("admin")
    @Test
    void addItemPage() throws Exception{
        mockMvc.perform(get("/admin/items/"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/items/index"))
                .andExpect(model().attributeExists("items"))
                .andExpect(authenticated().withRoles(Role.ADMIN.getRole()));
    }

    @DisplayName("아이템 화면 출력(접근 권한 없는 이용자) - 실패")
    @WithAccount("unauthenticateduser")
    @Test
    void addItemPageAccessForbidden() throws Exception{
        mockMvc.perform(get("/admin/items/index"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("아이템 추가 화면 - 카테고리 불러오기 성공")
    @WithAccount("admin")
    @Test
    void addItem_categoryPrint() throws Exception {
        Category category = Category.builder().name("라면").build();
        categoryRepository.save(category);
        mockMvc.perform(get("/admin/items/add"))
                .andExpect(model().attributeExists("category"));

    }

    @DisplayName("아이템 추가 - 성공")
    @WithAccount("admin")
    @Test
    void addItem() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.png",MediaType.IMAGE_PNG_VALUE, "Generate bytes to simulate a picture".getBytes());

        Category category = Category.builder().name("라면").build();
        categoryRepository.save(category);

        mockMvc.perform(multipart("/admin/items/add")
                .file(image)
                .param("name", "진라면")
                .param("stockQuantity", "1")
                .param("price", "1")
                .param("path", "라면")
                .param("cat", "라면")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/items/add"));

        assertThat(itemRepository.count()).isEqualTo(1);
    }

    @DisplayName("아이템 추가 - 실패")
    @WithAccount("admin")
    @Test
    void addItem_fail() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.png",MediaType.IMAGE_PNG_VALUE, "Generate bytes to simulate a picture".getBytes());

        Category category = Category.builder().name("라면").build();
        Item item = Item.builder()
                .name("진라면")
                .build();

        categoryRepository.save(category);
        itemRepository.save(item);

        mockMvc.perform(multipart("/admin/items/add")
                .file(image)
                .param("name", "진라면")
                .param("stockQuantity", "1")
                .param("price", "1")
                .param("path", "진라면")
                .param("cat", "라면")
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("itemForm", "name"))
                .andExpect(view().name("admin/items/add"));

        assertThat(itemRepository.count()).isEqualTo(1);
    }
}