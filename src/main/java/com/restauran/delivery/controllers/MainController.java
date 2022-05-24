package com.restauran.delivery.controllers;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.restauran.delivery.entity.FavouriteProduct;
import com.restauran.delivery.entity.Form;
import com.restauran.delivery.entity.PersonalData;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.User;
import com.restauran.delivery.repositories.FavouriteProductRepository;
import com.restauran.delivery.repositories.PersonalDataRepository;
import com.restauran.delivery.repositories.ProductsRepository;
import com.restauran.delivery.service.UserPrincipal;
import com.restauran.delivery.service.UserPrincipalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    FavouriteProductRepository fProductRepository;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    PersonalDataRepository personalDataRepository;

    @Autowired
    UserPrincipalService userService;

    private int getPrincipalId() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) auth.getPrincipal();

        if (user.getUsername().equals("anonymousUser")) {
            throw new Exception();
        }
        return user.getId();
    }

    private boolean hasFavProduct(int id, int productId) {
        Iterable<FavouriteProduct> all = fProductRepository.findAll();
        for (FavouriteProduct item : all) {
            if (item.getUserId() == id && item.getProductId() == productId) {
                return true;
            }
        }

        return false;
    }

	@GetMapping("/home")
	public String homeFirst(Model model) {
		model.addAttribute("title", "Главная страница");
        Iterable<ProductUnit> all = productsRepository.findAll();
        LinkedList<ProductUnit> best = new LinkedList<ProductUnit>();
        for (ProductUnit item: all) {
            if (item.getRating() >= 4) {
                best.add(item);
            }
        }
        model.addAttribute("bestProducts", best);
		return "home";
	}

    @GetMapping("/")
	public String homeSecond(Model model) {
		model.addAttribute("title", "Главная страница");
        Iterable<ProductUnit> all = productsRepository.findAll();
        LinkedList<ProductUnit> best = new LinkedList<ProductUnit>();
        for (ProductUnit item: all) {
            if (item.getRating() >= 4) {
                best.add(item);
            }
        }
        model.addAttribute("bestProducts", best);
		return "home";
	}

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/catalog")
    public String getCatalogPage(Model model) {
        
        Iterable<ProductUnit> all = productsRepository.findAll();
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
    public String addUser(@ModelAttribute Form userForm, Model model, RedirectAttributes atr) {
        
        User user  = new User(userForm);

        user = userService.saveUser(user);

        if (user == null) {
            atr.addFlashAttribute("error", 
                                "Пользователь с таким логином уже существует");
            return "redirect:/registration";
        }

        PersonalData personalData = new PersonalData(userForm);
        personalData.setUserId(user.getId());
        personalDataRepository.save(personalData);
        
        model.addAttribute("personal", personalData);

        return "/home";
    }

    @GetMapping("/catalog/item/{id}")
    public String getDetails(@PathVariable(value="id") int id, HttpServletRequest request, Model model){ 
        if (productsRepository.existsById(id) == false) {
            return "redirect:/catalog";
        }
        Map<String, ?> messages = RequestContextUtils.getInputFlashMap(request);
        if (messages != null) {
            String mes = (String) messages.get("message");
            model.addAttribute("message", mes);
        }
        ProductUnit product = productsRepository.findById(id).orElseThrow();

        model.addAttribute("product", product);
        int userId;
        try {
            userId = getPrincipalId();
            boolean tmp = hasFavProduct(userId, id);
            model.addAttribute("isFavourite", tmp);
        } catch (Exception e) {
            model.addAttribute("isFavourite", false);
        }
        return "/catalog/productItem";
    }
}
