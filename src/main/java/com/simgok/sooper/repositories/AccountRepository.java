package com.simgok.sooper.repositories;

import com.simgok.sooper.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Account findByEmail(String email);

    Account findByName(String name);

    Account readById(Long id);
}
