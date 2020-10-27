package com.simgok.sooper.repositories;

import com.simgok.sooper.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
