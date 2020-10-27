package com.simgok.sooper.repositories;

import com.simgok.sooper.model.Item;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(readOnly = true)
public interface ItemRepositoryExtension {
    List<Item> findByKeyword(String keyword);
}
