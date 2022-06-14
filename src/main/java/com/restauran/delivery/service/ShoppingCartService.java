package com.restauran.delivery.service;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.exceptions.NotEnoghElementsException;
import com.restauran.delivery.repositories.ShoppingCartRepository;

@Service
public class ShoppingCartService {

    ShoppingCartRepository cartRepository;
    
    public ShoppingCartService(ShoppingCartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public LinkedList<ShoppingCart> getUsersProducts(int userId) {

        Iterable<ShoppingCart> cart = cartRepository.findAll();
        LinkedList<ShoppingCart> products = new LinkedList<>();

        for (ShoppingCart c : cart) {
            if (userId == c.getUserId()) {
                products.add(c);
            }
        }

        return products;
    }

    private ShoppingCart getProductFromCart(int userId, int productId) 
                                                    throws NoSuchElementException {

        Iterable<ShoppingCart> cart = cartRepository.findAll();
        for (ShoppingCart item : cart) {
            if (item.getProductId() == productId && item.getUserId() == userId) {
                return item;
            }
        }

        throw new NoSuchElementException();
    }

    public void addProductToCart(ProductUnit product, int amount, int userId)  
                                                throws NotEnoghElementsException {
                                                            
        int curAmount = product.getAmount();
        if (curAmount < amount) {
            throw new NotEnoghElementsException(product.getName(), curAmount);
        }

        curAmount -= amount;
        product.setAmount(curAmount);

        ShoppingCart cart = null;
        try {
            cart = getProductFromCart(userId, product.getId());
            cart.setAmount(cart.getAmount() + amount);
        } catch (NoSuchElementException e) {
            cart = new ShoppingCart(product.getName(), product.getId(), userId, amount); 
        }

        cartRepository.save(cart);
    }

    public boolean isShoppingCartEmpty(int userId) {

        LinkedList<ShoppingCart> list = getUsersProducts(userId);
        
        if (list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteById(int productId, int userId) {

        Iterable<ShoppingCart> all = cartRepository.findAll();
        
        for (ShoppingCart item : all) {
            if (item.getProductId() == productId && item.getUserId() == userId) {
                cartRepository.deleteById(item.getId());
            }
        }
    }

    public void deleteById(int productId) {
        cartRepository.deleteById(productId);
    }

    public void clearUsersCart(int userId) {

        Iterable<ShoppingCart> all = cartRepository.findAll();
        
        
        for (ShoppingCart item : all) {
            if (item.getUserId() == userId) {
                cartRepository.deleteById(item.getId());
            }
        }
    }
}
