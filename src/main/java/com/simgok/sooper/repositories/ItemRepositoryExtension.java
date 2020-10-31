package com.simgok.sooper.repositories;

import com.simgok.sooper.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(readOnly = true)
public interface ItemRepositoryExtension {

    Page<Item> findByKeyword(String keyword, Pageable pageable);
    Page<Item> findAll(Pageable pageable);
}
