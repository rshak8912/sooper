package com.simgok.sooper.repositories;

import com.simgok.sooper.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.account.id= :id")
    List<Order> findAllById(@Param("id") Long id);

}
