package com.simgok.sooper.services;

import com.simgok.sooper.model.Account;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.model.Order;
import com.simgok.sooper.model.OrderItem;
import com.simgok.sooper.repositories.AccountRepository;
import com.simgok.sooper.repositories.ItemRepository;
import com.simgok.sooper.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final ItemRepository itemRepository;

    public Long order(Long accountId, Long itemId, int count) {

        Account account = accountRepository.findById(accountId).orElseGet(Account::new);
        Item item = itemRepository.findById(itemId).orElseGet(Item::new);

        OrderItem orderItem = createOrderItem(item, item.getPrice(), count);
        Order order = createOrder(account, orderItem);
        orderRepository.save(order);

        return order.getId();
    }

    public List<Order> getAllOrders(Account account) {
        return orderRepository.findAllById(account.getId());
    }

    public void cancelOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.orElseThrow(NoSuchElementException::new).cancel();
    }
    public Order createOrder(Account account, OrderItem... orderItems) {
        Order order = new Order();
        order.setAccount(account);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setOrderDate(LocalDateTime.now());

        return order;
    }
    public OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(orderPrice)
                .count(count)
                .build();
        item.removeStock(count);

        return orderItem;

    }

}
