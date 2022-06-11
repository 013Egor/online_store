package com.restauran.delivery.controllers.customer;

import java.util.LinkedList;

import com.restauran.delivery.entity.Order;
import com.restauran.delivery.interfaces.OrderManagerRead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CustomerController {

    @Autowired
    OrderManagerRead orderManager;

    @GetMapping("/customer/orders")
    public String getOrderPage(Model model) {

        LinkedList<Order> order = orderManager.getOrders();
        model.addAttribute("orders", order);

        if (order.size() == 0) {
            model.addAttribute("isEmpty", true);
        } else {
            model.addAttribute("isEmpty", false);
        }

        return "/customer/orders";
    }

    @GetMapping("/customer/order/{id}")
    public String getOrderItem(@PathVariable("id") int id, Model model) {

        model.addAttribute("products", orderManager.getOrdersItems(id));

        return "customer/orderItem";
    }
}
