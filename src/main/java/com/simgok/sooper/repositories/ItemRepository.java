package com.simgok.sooper.repositories;

import com.simgok.sooper.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>,ItemRepositoryExtension {
    boolean existsByName(String name);

    boolean existsByPath(String path);

}
