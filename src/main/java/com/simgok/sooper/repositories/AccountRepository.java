package com.simgok.sooper.repositories;

import com.simgok.sooper.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Account findByEmail(String email);

    Account findByNickName(String nickName);

    Account readById(Long id);
}
