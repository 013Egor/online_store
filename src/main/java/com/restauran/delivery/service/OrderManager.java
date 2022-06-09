package com.restauran.delivery.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.Order;
import com.restauran.delivery.entity.OrderItem;
import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.repositories.OrderItemsRepository;
import com.restauran.delivery.repositories.OrderRepository;

@Service
public class OrderManager {
    
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    ShoppingCartService shoppingCartService;

    public void order(int userId) {

        LinkedList<ShoppingCart> all = shoppingCartService.getUsersProducts(userId);
        if (all.size() == 0) {
            throw new NoSuchElementException();
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
                shoppingCartService.deleteById(item.getId(), userId);
            }
        }
    }

    public void clearOrders(int userId) {
        
        clearOrderItemRepository(userId);
        clearOrderRepository(userId);
    }

    private void clearOrderItemRepository(int userId) {

        Iterable<OrderItem> all = orderItemsRepository.findAll();
        
        for (OrderItem item : all) {
            if (item.getUserId() == userId) {
                orderItemsRepository.deleteById(item.getId());
            }
        }
    }

    private void clearOrderRepository(int userId) {

        Iterable<Order> all = orderRepository.findAll();
        
        for (Order item : all) {
            if (item.getUserId() == userId) {
                orderRepository.deleteById(item.getId());
            }
        }
    }

    public LinkedList<Order> getUsersOrders(int userId) {

        Iterable<Order> all = orderRepository.findAll();
        LinkedList<Order> order = new LinkedList<Order>();
        
        for (Order item : all) {
            if (item.getUserId() == userId) {
                order.add(item);
            }
        }

        return order;
    }

    public LinkedList<OrderItem> getOrdersItems(int id) {

        Iterable<OrderItem> all = orderItemsRepository.findAll();
        LinkedList<OrderItem> products = new LinkedList<OrderItem>();
        
        for (OrderItem item: all) {
            if (item.getOrderNum() == id) {
                products.add(item);
            }
        }

        return products;
    }
}
