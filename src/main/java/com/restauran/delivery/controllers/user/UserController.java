package com.restauran.delivery.controllers.user;

import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import com.restauran.delivery.entity.FavouriteProduct;
import com.restauran.delivery.entity.Form;
import com.restauran.delivery.entity.PersonalData;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.entity.User;
import com.restauran.delivery.exceptions.NotEnoghElementsException;
import com.restauran.delivery.service.FavouriteProductService;
import com.restauran.delivery.service.OrderManager;
import com.restauran.delivery.service.PersonalDataService;
import com.restauran.delivery.service.ProductService;
import com.restauran.delivery.service.ShoppingCartService;
import com.restauran.delivery.service.UserPrincipalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class UserController {

    @Autowired
    PersonalDataService personalData;

    @Autowired
    UserPrincipalService userService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    OrderManager orderManager;

    @Autowired
    FavouriteProductService favouriteProductService;    

    @Autowired
    ProductService productService;

    @GetMapping("/user")
    public String getUserPage(Model model) throws Exception {
        
        PersonalData data = personalData.getPersonalData(userService.getPrincipalId());

        model.addAttribute("personal", data);
        model.addAttribute("title", "Страница пользователя");

        return "/user/user";
    }

    @GetMapping("/user/favourite")
    public String getFavPage(Model model) throws Exception {
        
        Iterable<ProductUnit> products = 
            favouriteProductService.getFavProducts(userService.getPrincipalId());

        model.addAttribute("products", products);
        
        return "/user/favourite";
    }

    @GetMapping("/user/currentOrders")
    public String getOrederPage(Model model) throws Exception {
        
        model.addAttribute("orders", 
            orderManager.getUsersOrders(userService.getPrincipalId()));

        return "/user/currentOrders";
    }

    @GetMapping("/user/shoppingCart")
    public String getCartPage(Model model) throws Exception {

        LinkedList<ShoppingCart> products = 
            shoppingCartService.getUsersProducts(userService.getPrincipalId());

        model.addAttribute("products", products);
        
        if (products.size() == 0) {
            model.addAttribute("isEmpty", true);
        } else {
            model.addAttribute("isEmpty", false);
        }
        
        return "/user/shoppingCart";
    }

    @GetMapping("/user/settings")
    public String getSettingsPage(HttpServletRequest request, Model model) {

        model.addAttribute("form", new Form());

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            String error = (String) inputFlashMap.get("error");
            model.addAttribute("error", error);
        }
        
        return "/user/settings";
    }

    @PostMapping("/user/settings")
    public String setNewUserData(@ModelAttribute Form form) throws Exception {

        PersonalData data = personalData.getPersonalData(userService.getPrincipalId());
        
        data.setNewData(form);
        personalData.save(data);

        return "redirect:/user";
    }

    @PostMapping("/user/changePassword")
    public String changePassword(@RequestParam("oldPassword") String password,
                                @RequestParam("password") String newPassword,
                                Model model, RedirectAttributes atr) throws Exception {

        User user = userService.savePassword(userService.getPrincipalId(), password, newPassword);
        
        if (user == null) {
            atr.addFlashAttribute("error", "Пароль не изменён, так как прежний пароль введён неправильно");
            return "redirect:/user/settings";
        }
        
        return "redirect:/user";
    }

    @PostMapping("/user/item/{id}/setRating")
    public String setRating(@PathVariable("id") int id, 
                        @RequestParam("rating") int rating, Model model) {

        try {
            productService.setNewRating(id, rating);
        } catch (NoSuchElementException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        return "redirect:/catalog/item/{id}";
    }

    @PostMapping("/user/item/{id}/toCart")
    public String addToCart(@PathVariable("id") int id, 
                    @RequestParam("cart") int amount, Model model, 
                    RedirectAttributes atr) throws Exception {

        try {
            ProductUnit product = productService.getProductById(id);
            shoppingCartService.addProductToCart(product, amount, userService.getPrincipalId());
        } catch (NoSuchElementException e) {
            model.addAttribute("message", e.getMessage());

            return "error";

        }catch(NotEnoghElementsException e) {
            atr.addFlashAttribute("message", "We have just " + e.getAmount() + " of " + e.getName());
            
            return "redirect:/catalog/item/{id}";
        }

        return "redirect:/catalog/item/{id}";
    }

    @GetMapping("/user/item/{id}/delFromCart")
    public String delFromCart(@PathVariable("id") int id) throws Exception {

        shoppingCartService.deleteById(id, userService.getPrincipalId());

        return "redirect:/user/shoppingCart";
    }
    
    @GetMapping("/user/shoppingCart/order")
    public String orderCart() throws Exception {
        
        try {
            orderManager.order(userService.getPrincipalId());
            return "redirect:/user/currentOrders";
        } catch (NoSuchElementException e) {
            return "redirect:/user/shoppingCart";
        }        
    }

    @GetMapping("/user/item/{id}/toFavour")
    public String addToFavour(@PathVariable("id") int id) throws Exception {

        FavouriteProduct product = new FavouriteProduct(id, userService.getPrincipalId());
        favouriteProductService.save(product);

        return "redirect:/catalog/item/{id}";
    }

    @GetMapping("/user/item/{id}/delFromFavour")
    public String delFromFavour(@PathVariable("id") int id, HttpServletRequest request) {
        
        try {
            favouriteProductService.deleteById(id, userService.getPrincipalId());
        } catch (Exception e) {}

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/user/order/{id}")
    public String getOrderItem(@PathVariable("id") int id, Model model) {

        model.addAttribute("products", 
            orderManager.getOrdersItems(id));

        return "user/orderItem";
    }

    @GetMapping("/user/order/{id}/delete")
    public String deleteCompletedOrder(@PathVariable("id") int id) {
        
        orderManager.deleteCompletedOrder(id);

        return "redirect:/user/currentOrders";
    }

    @GetMapping("/user/delete")
    public String deleteUser() throws Exception {

        int userId = userService.getPrincipalId();
        
        favouriteProductService.clearUsersFavours(userId);
        shoppingCartService.clearUsersCart(userId);
        personalData.deleteById(userId);
        orderManager.clearOrders(userId);
        
        userService.delete(userId);

        return "redirect:/logout";
    }    
}