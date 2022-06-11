package com.restauran.delivery.controllers.customer;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.restauran.delivery.interfaces.OrderManagerWrite;

@Controller
public class OrderHandler {

    @Autowired
    OrderManagerWrite orderManager;
    
    @GetMapping("/customer/order/{id}/take")
    public String takeOrderOff(@PathVariable("id") int id) {

        try {
            orderManager.takeOrderOff(id);
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
        

        return "redirect:/customer/orders";
    }
}
