package com.restauran.delivery.controllers;

import java.util.LinkedList;

import com.restauran.delivery.entity.CompletedOrderItem;
import com.restauran.delivery.entity.Order;
import com.restauran.delivery.entity.OrderItem;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.repositories.CompletedOrdersRepository;
import com.restauran.delivery.repositories.OrderItemsRepository;
import com.restauran.delivery.repositories.OrderRepository;
import com.restauran.delivery.repositories.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CustomerController {
    
    @Autowired
    CompletedOrdersRepository cOrdersRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;
    
    @Autowired
    ProductsRepository productsRepository;


    private CompletedOrderItem getCompletedOrderItem(Iterable<CompletedOrderItem> completedOrders, int id) {
        for (CompletedOrderItem cItem : completedOrders) {
            if (id == cItem.getProductId()) {
               return cItem; 
            }
        }
        return null;
    }

    @GetMapping("/customer/orders")
    public String getOrderPage(Model model) {
        Iterable<Order> all = orderRepository.findAll();
        LinkedList<Order> order = new LinkedList<Order>();
        for (Order item : all) {
            if (item.isDelivered() == false) {
                order.add(item);
            }
        }
        model.addAttribute("orders", order);
        if (order.size() == 0) {
            model.addAttribute("isEmpty", true);
        } else {
            model.addAttribute("isEmpty", false);
        }
        return "/customer/orders";
    }

    
    @GetMapping("/customer/order/{id}/take")
    public String takeOffOrder(@PathVariable("id") int id) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setDelivered(true);
        Iterable<OrderItem> all = orderItemsRepository.findAll();
        Iterable<CompletedOrderItem> completedOrders = cOrdersRepository.findAll();
        CompletedOrderItem orderItem;
        for (OrderItem item : all) {
            if (item.getOrderNum() == id) {
                orderItem = null;
                orderItem = getCompletedOrderItem(completedOrders, item.getProductId());
                if (orderItem == null) {
                    orderItem = new CompletedOrderItem();
                    orderItem.setName(item.getName());
                    orderItem.setProductId(item.getProductId());
                }
                ProductUnit productUnit = productsRepository.findById(item.getProductId()).orElseThrow();
                orderItem.setAmount(orderItem.getAmount() + item.getAmount());
                orderItem.setRating(productUnit.getRating());
                orderItem.setPrice(productUnit.getPrice() * orderItem.getAmount());
                cOrdersRepository.save(orderItem);
            }
        }
        orderRepository.save(order);
        return "redirect:/customer/orders";
    }

    @GetMapping("/customer/order/{id}")
    public String getOrderItem(@PathVariable("id") int id, Model model) {
        Iterable<OrderItem> all = orderItemsRepository.findAll();
        LinkedList<OrderItem> products = new LinkedList<OrderItem>();
        for (OrderItem item: all) {
            if (item.getOrderNum() == id) {
                products.add(item);
            }
        }

        model.addAttribute("products", products);

        return "customer/orderItem";
    }
}
