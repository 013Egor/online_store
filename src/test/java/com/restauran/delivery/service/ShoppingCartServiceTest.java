package com.restauran.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.exceptions.NotEnoghElementsException;
import com.restauran.delivery.fakes.databases.FakeCartRep;


public class ShoppingCartServiceTest {

    FakeCartRep cartRepository = new FakeCartRep();
    ShoppingCartService service = new ShoppingCartService(cartRepository);

    @Test
    void testAddProductToCart() {

        cartRepository.deleteAll();

        int userId = 0, productId = 0;
        long amountBefore = cartRepository.count();

        ProductUnit firstProduct = new ProductUnit();
        firstProduct.setAmount(3);
        firstProduct.setId(productId);

        assertThrowsExactly(NotEnoghElementsException.class, ()->{
            service.addProductToCart(firstProduct, 4, userId);
        });

        service.addProductToCart(firstProduct, 2, userId);
        assertEquals(amountBefore + 1, cartRepository.count());

        service.addProductToCart(firstProduct, 1, userId);
        assertEquals(amountBefore + 1, cartRepository.count());
    }

    @Test
    void testGetUsersProducts() {

        cartRepository.deleteAll();
        
        int amount = 4;
        ShoppingCart[] products = new ShoppingCart[amount];
        for (int i = 0; i < amount; i++) {
            products[i] = new ShoppingCart("-", i, i%2, amount);
            cartRepository.save(products[i]);
        }

        LinkedList<ShoppingCart> usersProducts = service.getUsersProducts(1);
        assertEquals(2, usersProducts.size());
    }

    @Test
    void testIsShoppingCartEmpty() {
        cartRepository.deleteAll();
        int userId = 0;
        assertTrue(service.isShoppingCartEmpty(userId));
        cartRepository.save(new ShoppingCart("-", 1, userId, 1));
        assertFalse(service.isShoppingCartEmpty(userId));
    }
}
