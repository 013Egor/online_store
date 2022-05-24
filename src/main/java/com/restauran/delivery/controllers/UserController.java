package com.restauran.delivery.controllers;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import com.restauran.delivery.entity.FavouriteProduct;
import com.restauran.delivery.entity.Form;
import com.restauran.delivery.entity.IData;
import com.restauran.delivery.entity.Order;
import com.restauran.delivery.entity.OrderItem;
import com.restauran.delivery.entity.PersonalData;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.entity.User;
import com.restauran.delivery.repositories.FavouriteProductRepository;
import com.restauran.delivery.repositories.OrderItemsRepository;
import com.restauran.delivery.repositories.OrderRepository;
import com.restauran.delivery.repositories.PersonalDataRepository;
import com.restauran.delivery.repositories.ProductsRepository;
import com.restauran.delivery.repositories.ShoppingCartRepository;
import com.restauran.delivery.service.UserPrincipal;
import com.restauran.delivery.service.UserPrincipalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    FavouriteProductRepository favProdRepository;

    @Autowired
    ShoppingCartRepository cartRepository;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    PersonalDataRepository pDataRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserPrincipalService userService;

    private int getPrincipalId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) auth.getPrincipal();
        return user.getId();
    }

    private PersonalData getPersonalData(int id) {
        Iterable<PersonalData> temp = pDataRepository.findAll();
        Iterator<PersonalData> it = temp.iterator();
        PersonalData data = null;

        while (it.hasNext()) {
            data = it.next();
            if (data.getUserId() == id) {
                return data;
            }
        }

        return null;
    }

    private LinkedList<ShoppingCart> getCartsProducts() {
        Iterable<ShoppingCart> cart = cartRepository.findAll();
        LinkedList<ShoppingCart> products = new LinkedList<>();
        int id = getPrincipalId();
        for (ShoppingCart c : cart) {
            if (id == c.getUserId()) {
                products.add(c);
            }
        }

        return products;
    }

    private LinkedList<Integer> getFavProducts() {
        Iterable<FavouriteProduct> fav = favProdRepository.findAll();
        LinkedList<Integer> products = new LinkedList<>();
        int id = getPrincipalId();
        for (FavouriteProduct fProduct : fav) {
            if (id == fProduct.getUserId()) {
                products.add(fProduct.getProductId());
            }
        }

        return products;
    }

    private LinkedList<ProductUnit> getProductsById(Iterable<Integer> ids) {
        LinkedList<ProductUnit> products = new LinkedList<ProductUnit>();
        ProductUnit temp;
        for (Integer id : ids) {
            temp = productsRepository.findById(id).orElseThrow();
            products.add(temp);
        }

        return products;
    }

    private int getFavProductId(int id) throws Exception {
        Iterable<FavouriteProduct> all = favProdRepository.findAll();
        for (FavouriteProduct item : all) {
            if (item.getProductId() == id) {
                return item.getId();
            }
        }

        throw new Exception();
    }

    private ShoppingCart getProductCart(int userId, int productId) {
        Iterable<ShoppingCart> cart = cartRepository.findAll();
        for (ShoppingCart item : cart) {
            if (item.getProductId() == productId && item.getUserId() == userId) {
                return item;
            }
        }

        return null;
    }

    
    @GetMapping("/user")
    public String getUserPage(Model model) {
        
        PersonalData data = getPersonalData(getPrincipalId());
        model.addAttribute("personal", data);
        model.addAttribute("title", "Страница пользователя");
        return "/user/user";
    }

    @GetMapping("/user/favourite")
    public String getFavPage(Model model) {
        LinkedList<Integer> favouriteProducts = getFavProducts();
        Iterable<ProductUnit> products = getProductsById(favouriteProducts);
        model.addAttribute("products", products);
        return "/user/favourite";
    }

    @GetMapping("/user/currentOrders")
    public String getOrederPage(Model model) {
        Iterable<Order> all = orderRepository.findAll();
        LinkedList<Order> order = new LinkedList<Order>();
        int userId = getPrincipalId();
        for (Order item : all) {
            if (item.getUserId() == userId) {
                order.add(item);
            }
        }
        model.addAttribute("orders", order);
        return "/user/currentOrders";
    }

    @GetMapping("/user/shoppingCart")
    public String getCartPage(Model model) {
        LinkedList<ShoppingCart> products = getCartsProducts();
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
    public String setNewUserData(@ModelAttribute Form form) {
        PersonalData data = getPersonalData(getPrincipalId());
        data.setNewData(form);
        pDataRepository.save(data);

        return "redirect:/user";
    }

    @PostMapping("/user/changePassword")
    public String changePassword(@RequestParam("oldPassword") String password,
                                @RequestParam("password") String newPassword,
                                Model model, RedirectAttributes atr) {
        User user = userService.savePassword(getPrincipalId(), password, newPassword);
        if (user == null) {
            atr.addFlashAttribute("error", "Пароль не изменён, так как прежний пароль введён неправильно");
            return "redirect:/user/settings";
        }
        return "redirect:/user";
    }


    @PostMapping("/user/item/{id}/setRating")
    public String setRating(@PathVariable("id") int id, @RequestParam("rating") int rating, Model model) {
        ProductUnit product;
        try {
            product = productsRepository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        double newRating = product.getRating() * product.getVotingAmout();
        product.increaseVotingAmount();
        newRating += rating;
        newRating = newRating / product.getVotingAmout();
        product.setRating(newRating);
        productsRepository.save(product);

        return "redirect:/catalog/item/{id}";
    }

    @PostMapping("/user/item/{id}/toCart")
    public String addToCart(@PathVariable("id") int id, @RequestParam("cart") int amount, Model model, RedirectAttributes atr) {
        ProductUnit product;
        try {
            product = productsRepository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        int curAmount = product.getAmount();
        if (curAmount < amount) {
            atr.addFlashAttribute("message", "We have just " + curAmount + " of " + product.getName());
            return "redirect:/catalog/item/{id}";
        }
        curAmount -= amount;
        product.setAmount(curAmount);
        ShoppingCart cart = getProductCart(getPrincipalId(), id);
        if (cart == null) {
            cart = new ShoppingCart(product.getName(), id, getPrincipalId(), amount); 
        } else {
            cart.setAmount(cart.getAmount() + amount);
        }
        cartRepository.save(cart);

        return "redirect:/catalog/item/{id}";
    }

    @GetMapping("/user/item/{id}/delFromCart")
    public String delFromCart(@PathVariable("id") int id) {
        Iterable<ShoppingCart> all = cartRepository.findAll();
        int userId = getPrincipalId();
        for (ShoppingCart item : all) {
            if (item.getProductId() == id && item.getUserId() == userId) {
                cartRepository.deleteById(item.getId());
            }
        }

        return "redirect:/user/shoppingCart";
    }

    private boolean isShoppingCartEmpty(Iterable<ShoppingCart> list) {
        
        for (ShoppingCart item : list) {
            item.getName();
            return false;
        }
        return true;
    }

    @GetMapping("/user/shoppingCart/order")
    public String orderCart() {
        int userId = getPrincipalId();
        Iterable<ShoppingCart> all = cartRepository.findAll();
        if (isShoppingCartEmpty(all)) {
            return "redirect:/user/shoppingCart";
        }
        GregorianCalendar cal = new GregorianCalendar();
        Order savedOrder = orderRepository.save(new Order(userId, cal.get(Calendar.YEAR),
                                                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
        for (ShoppingCart item : all) {
            if (item.getUserId() == userId) {
                OrderItem order = new OrderItem(item.getName(), item.getProductId(), item.getAmount());
                order.setOrderNum(savedOrder.getId());
                order.setUserId(userId);
                orderItemsRepository.save(order);
                cartRepository.deleteById(item.getId());
            }
        }
        return "redirect:/user/currentOrders";
    }

    @GetMapping("/user/item/{id}/toFavour")
    public String addToFavour(@PathVariable("id") int id) {
        FavouriteProduct product = new FavouriteProduct(id, getPrincipalId());
        favProdRepository.save(product);

        return "redirect:/catalog/item/{id}";
    }

    @GetMapping("/user/item/{id}/delFromFavour")
    public String delFromFavour(@PathVariable("id") int id) {
        try {
            int favId = getFavProductId(id);
            favProdRepository.deleteById(favId);
        } catch (Exception e) {}

        return "redirect:/catalog/item/{id}";
    }

    @GetMapping("/user/order/{id}")
    public String getOrderItem(@PathVariable("id") int id, Model model) {
        Iterable<OrderItem> all = orderItemsRepository.findAll();
        LinkedList<OrderItem> products = new LinkedList<OrderItem>();
        for (OrderItem item: all) {
            if (item.getOrderNum() == id) {
                products.add(item);
            }
        }

        model.addAttribute("products", products);

        return "user/orderItem";
    }

    @GetMapping("/user/delete")
    public String deleteUser() {
        clearTable(favProdRepository);
        clearTable(cartRepository);
        clearTable(pDataRepository);
        clearTable(orderItemsRepository);
        clearTable(orderRepository);
        userService.delete(getPrincipalId());

        return "redirect:/logout";
    }

    public void clearTable(CrudRepository<?, Integer> repository) {
        Iterable<IData> all =(Iterable<IData>) repository.findAll();
        int userId = getPrincipalId();
        for (IData item : all) {
            if (item.getUserId() == userId) {
                repository.deleteById(item.getId());
            }
        }
    }
    
}