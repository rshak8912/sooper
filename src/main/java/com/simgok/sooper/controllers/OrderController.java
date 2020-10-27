package com.simgok.sooper.controllers;

import com.simgok.sooper.model.Account;
import com.simgok.sooper.model.Order;
import com.simgok.sooper.services.OrderService;
import com.simgok.sooper.settings.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /*
     *
     * 주문 내역 화면
     * */
    @GetMapping("/order/view")
    public String orderPage(@CurrentUser Account account, Model model) {
        List<Order> orders = orderService.getAllOrders(account);
        model.addAttribute("orders", orders);
        return "order/view";
    }



    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);

        return "redirect:/order";
    }

    @PostMapping("/order/{accountId}/item/{itemId}")
    public String orderPage(@PathVariable("accountId") Long accountId, @PathVariable("itemId") Long itemId){
        int count = 1;
        orderService.order(accountId, itemId, count = 1);
        return "redirect:/order/view";
    }
  /*  *//*
     * 주문 내역 저장
     * *//*
    @PostMapping("/order")
    public String order(@CurrentUser Account account, Item item, @RequestParam("count") int count) {
        orderService.order(account, item, count);
        return "redirect:/order";
    }*/

}
