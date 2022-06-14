package com.restauran.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.restauran.delivery.entity.Order;
import com.restauran.delivery.entity.OrderItem;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.fakes.databases.FakeCartRep;
import com.restauran.delivery.fakes.databases.FakeCompletetedOrders;
import com.restauran.delivery.fakes.databases.FakeOrderItemsRep;
import com.restauran.delivery.fakes.databases.FakeOrderRep;
import com.restauran.delivery.fakes.databases.FakeProductRep;

public class OrderManagerTest {

    FakeOrderRep orderRep = new FakeOrderRep();
    FakeOrderItemsRep orderItemsRep = new FakeOrderItemsRep();
    FakeCartRep shoppincCart = new FakeCartRep();
    ShoppingCartService cartService = new ShoppingCartService(shoppincCart);
    FakeCompletetedOrders completetedOrdersRep = new FakeCompletetedOrders();
    FakeProductRep productsRepository = new FakeProductRep();
    OrderManager manager = new OrderManager(orderRep,
         orderItemsRep, cartService, completetedOrdersRep, productsRepository);
    
    
    private void setValues() {
        String[] names = {
            "One", "Two", "Three"
        };

        for (int i = 0; i < 10; i++) {
            orderItemsRep.save(new OrderItem(names[i%3], i%3, i, i));
            orderRep.save(new Order(i, true));
            shoppincCart.save(new ShoppingCart(names[i%3], i%3, i, i));
        }
    }

    @Test
    void testClearOrders() {

        orderItemsRep.deleteAll();
        orderRep.deleteAll();

        setValues();

        int userId = 0;

        manager.clearOrders(userId);

        Iterable<OrderItem> items = orderItemsRep.findAll();
        Iterable<Order> orders = orderRep.findAll();
        boolean actual = false;
        for (Order order2 : orders) {
            if (order2.getUserId() == userId) {
                actual = true;
                break;
            }
        }

        for (OrderItem orderItem : items) {
            if (orderItem.getUserId() == userId || actual) {
                actual = true;
                break;
            }
        }

        assertFalse(actual);
    }

    @Test
    void testDeleteCompletedOrder() {

        orderItemsRep.deleteAll();
        orderRep.deleteAll();

        setValues();

        int orderId = 1;

        manager.deleteCompletedOrder(orderId);
        
        assertFalse(orderRep.existsById(orderId));
        Iterable<OrderItem> all = orderItemsRep.findAll();
        boolean actual = false;
        for (OrderItem orderItem : all) {
            if (orderItem.getOrderNum() == orderId) {
                actual = true;
            }
        }

        assertFalse(actual);
        
    }

    @Test
    void testDeleteShoppingCartOrders() {

        orderItemsRep.deleteAll();
        orderRep.deleteAll();

        setValues();

        int userId = 2;
        Iterable<ShoppingCart> before = shoppincCart.findAll();
        boolean actual = false;
        for (ShoppingCart shoppingCart : before) {
            if (shoppingCart.getUserId() == userId) {
                actual = true;
            }
        }
        assertTrue(actual);

        manager.deleteShoppingCartOrders(userId);

        Iterable<ShoppingCart> after = shoppincCart.findAll();
        actual = false;
        for (ShoppingCart shoppingCart : after) {
            if (shoppingCart.getUserId() == userId) {
                actual = true;
            }
        }
        assertFalse(actual);
    }

    @Test
    void testGetOrders() {
        orderRep.deleteAll();

        for (int i = 0; i < 3; i++) {
            orderRep.save(new Order(i, true));
        }
        for (int i = 0; i < 2; i++) {
            orderRep.save(new Order(i, false));
        }

        LinkedList<Order> orders = manager.getOrders();
        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrdersItems() {
        orderItemsRep.deleteAll();

        for (int i = 0; i < 3; i++) {
            orderItemsRep.save(new OrderItem("-", i, 1, 1));
        }
        for (int i = 0; i < 2; i++) {
            orderItemsRep.save(new OrderItem("-", i + 3, 1, 2));
        }

        LinkedList<OrderItem> orders = manager.getOrdersItems(2);
        assertEquals(2, orders.size());
    }

    @Test
    void testGetUsersOrders() {
        orderRep.deleteAll();

        for (int i = 0; i < 3; i++) {
            orderRep.save(new Order(i, true));
        }
        for (int i = 0; i < 2; i++) {
            orderRep.save(new Order(i, false));
        }

        LinkedList<Order> orders = manager.getUsersOrders(0);
        assertEquals(2, orders.size());
    }

    @Test
    void testOrder() {
        shoppincCart.deleteAll();
        productsRepository.deleteAll();
        orderRep.deleteAll();
        orderItemsRep.deleteAll();

        for (int i = 0; i < 3; i++) {
            productsRepository.save(new ProductUnit("-", 1, 
                            "-", "-", 3, 3));
        }

        int userId = 0;
        assertThrowsExactly(NoSuchElementException.class, () -> {
            manager.order(userId);
        });

        for (int i = 0; i < 2; i++) {
            shoppincCart.save(new ShoppingCart("", i, userId, 1));
        }
        for (int i = 0; i < 3; i++) {
            shoppincCart.save(new ShoppingCart("", i, userId + 1, 1));
        }
        
        assertThrowsExactly(NoSuchElementException.class, () -> {
            manager.order(10);
        });

        shoppincCart.deleteById(3);
        manager.order(userId);
        assertEquals(1, orderRep.count());
        assertEquals(2, orderItemsRep.count());
    }

    @Test
    void testTakeOrderOff() {
        orderRep.deleteAll();
        orderItemsRep.deleteAll();
        completetedOrdersRep.deleteAll();
        productsRepository.deleteAll();

        assertThrowsExactly(NoSuchElementException.class, () -> {
            manager.takeOrderOff(0);
        });
        int orderId = 0;
        orderRep.save(new Order(orderId, false));
        for (int i = 0; i < 5; i++) {
            orderItemsRep.save(new OrderItem("name", i, 1, orderId));
            productsRepository.save(new ProductUnit());
        }
        for (int i = 0; i < 3; i++) {
            orderItemsRep.save(new OrderItem("name", i, 1, orderId + 1));
        }

        manager.takeOrderOff(orderId);
        assertEquals(5, completetedOrdersRep.count());
        assertEquals(1, orderRep.count());

        int orderId2 = 1;
        orderRep.save(new Order(orderId2, false));
        for (int i = 0; i < 5; i++) {
            orderItemsRep.save(new OrderItem("name", i, 1, orderId2));
        }
        for (int i = 0; i < 3; i++) {
            orderItemsRep.save(new OrderItem("name", i, 1, orderId2 + 1));
        }

        manager.takeOrderOff(orderId2);
        assertEquals(5, completetedOrdersRep.count());
        assertEquals(2, orderRep.count());
    }
}
