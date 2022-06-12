package com.restauran.delivery.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.restauran.delivery.entity.Form;
import com.restauran.delivery.entity.PersonalData;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.User;
import com.restauran.delivery.service.FavouriteProductService;
import com.restauran.delivery.service.PersonalDataService;
import com.restauran.delivery.service.ProductService;
import com.restauran.delivery.service.UserPrincipalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;


@Controller
public class MainController {

    @Autowired
    PersonalDataService personalDataService;

    @Autowired
    UserPrincipalService userService;

    @Autowired
    FavouriteProductService favProductService;

    @Autowired
    ProductService productService;


    private void sendErrorMessage(HttpServletRequest request, Model model) {
        Map<String, ?> messages = RequestContextUtils.getInputFlashMap(request);
        if (messages != null) {
            String mes = (String) messages.get("message");
            model.addAttribute("message", mes);
        }
    }

    private void sendProductInfo (Model model, int id) {
        ProductUnit product = productService.getProductById(id);

        model.addAttribute("product", product);
        try {
            boolean tmp = favProductService.isFavourite(userService.getPrincipalId(), id);
            model.addAttribute("isFavourite", tmp);
        } catch (Exception e) {
            model.addAttribute("isFavourite", false);
        }
        
    }

    

	@GetMapping("/home")
	public String homeFirst(Model model) {

		model.addAttribute("title", "Главная страница");
        model.addAttribute("bestProducts", productService.getBestProducts());

		return "home";
	}

    @GetMapping("/")
	public String homeSecond(Model model) {
		return "redirect:/home";
	}

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/error")
    public String error() {
        return "/error";
    }

    @GetMapping("/catalog")
    public String getCatalogPage(Model model) {
        
        Iterable<ProductUnit> all = productService.getAll();
        model.addAttribute("products", all);

        return "/catalog/catalog";
    }

    @GetMapping("/registration")
    public String registration(Model model, HttpServletRequest request) {

        model.addAttribute("userForm", new Form());
        Map<String, ?> error = RequestContextUtils.getInputFlashMap(request);

        if (error != null) {
            String message = (String) error.get("error");
            model.addAttribute("error", message);
        }

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute Form userForm, Model model, 
                        RedirectAttributes atr) {
        
        User user  = new User(userForm);

        user = userService.saveUser(user);

        if (user == null) {
            atr.addFlashAttribute("error", 
                                "Пользователь с таким логином уже существует");
            return "redirect:/registration";
        }

        PersonalData personalData = new PersonalData(userForm);
        personalData.setUserId(user.getId());
        personalDataService.save(personalData);
        
        model.addAttribute("personal", personalData);

        return "/home";
    }

    @GetMapping("/catalog/item/{id}")
    public String getDetails(@PathVariable(value="id") int id, 
                        HttpServletRequest request, Model model) { 

        if (productService.existsById(id) == false) {
            return "redirect:/catalog";
        }

        sendErrorMessage(request, model);
        sendProductInfo(model, id);
        
        return "/catalog/productItem";
    }
}
