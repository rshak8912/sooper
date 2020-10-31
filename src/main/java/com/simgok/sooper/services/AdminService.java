package com.simgok.sooper.services;

import com.simgok.sooper.model.Category;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.repositories.CategoryRepository;
import com.simgok.sooper.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public void saveNewCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseGet(Category::new);
    }

  /*  public Item createNewItem(ItemForm itemForm) {

        Item newItem = itemRepository.save(item);
        return newItem;
    }*/

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public boolean checkFileExtension(String filename) {
        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            return true;
        } else {
            return false;
        }
    }


    public void saveImage(String image, byte[] bytes) throws IOException {
        Path path = Paths.get("src/main/resources/static/media/" + image);
        Files.write(path, bytes);
    }

    public void saveItem(String image, Item newItem) {
        newItem.setImage(image);
        itemRepository.save(newItem);
    }

    public Optional<Item> findItem(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public Item getEditItem(Long itemId) {
        return itemRepository.findById(itemId).orElseGet(Item::new);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseGet(Item::new);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Item findItemByName(String name) {
        return itemRepository.findByName(name);
    }

    public Long getItemIdByName(String originalItemName) {
        return itemRepository.findByName(originalItemName).getId();
    }
}
