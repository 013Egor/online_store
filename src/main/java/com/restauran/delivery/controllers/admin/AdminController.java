package com.restauran.delivery.controllers.admin;

import com.restauran.delivery.entity.CompletedOrderItem;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.interfaces.OrderManagerRead;
import com.restauran.delivery.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
	ProductService productService;

    @Autowired
    OrderManagerRead orderManager;

    @GetMapping("/admin/statistic")
    public String getStatisticPage(Model model) {

        model.addAttribute("title", "Страница администратора");
        Iterable<CompletedOrderItem> all = orderManager.getCompletedOrders();
        model.addAttribute("products", all);

        return "/admin/admin";
    }

    
    @GetMapping("/admin/catalog")
    public String getCatalogPage(Model model) {

        model.addAttribute("product", new ProductUnit());

        Iterable<ProductUnit> products = productService.getAll();
        model.addAttribute("allProducts", products);

        return "/admin/alteringCatalog";
    }
}