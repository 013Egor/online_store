package com.restauran.delivery.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.CompletedOrderItem;
import com.restauran.delivery.entity.Order;
import com.restauran.delivery.entity.OrderItem;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.interfaces.OrderManagerRead;
import com.restauran.delivery.interfaces.OrderManagerWrite;
import com.restauran.delivery.repositories.CompletedOrdersRepository;
import com.restauran.delivery.repositories.OrderItemsRepository;
import com.restauran.delivery.repositories.OrderRepository;
import com.restauran.delivery.repositories.ProductsRepository;

@Service
public class OrderManager implements OrderManagerRead, OrderManagerWrite {
    
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    CompletedOrdersRepository cOrdersRepository;

    @Autowired
    ProductsRepository productsRepository;

    public void order(int userId) {

        LinkedList<ShoppingCart> all = shoppingCartService.getUsersProducts(userId);
        if (all.size() == 0) {
            throw new NoSuchElementException();
        }
        
        GregorianCalendar cal = new GregorianCalendar();
        Order savedOrder = orderRepository.save(new Order(userId, cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
        LinkedList<OrderItem> ordersProducts = new LinkedList<OrderItem>();
        
        for (ShoppingCart item : all) {
            if (item.getUserId() == userId) {
                if (productsRepository.existsById(item.getProductId()) == false) {
                    orderRepository.deleteById(savedOrder.getId());
                    deleteShoppingCartOrders(userId);
                    throw new NoSuchElementException("Данный товар не доступен для заказа");
                }
                OrderItem order = new OrderItem(item.getName(), item.getProductId(), item.getAmount());
                order.setOrderNum(savedOrder.getId());
                order.setUserId(userId);
                ordersProducts.add(order);
                shoppingCartService.deleteById(item.getProductId(), userId);
            }
        }

        for (OrderItem item : ordersProducts) {
            orderItemsRepository.save(item);  
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

    public Iterable<CompletedOrderItem> getCompletedOrders() {

        Iterable<CompletedOrderItem> all = cOrdersRepository.findAll();

        return all;
    }

    public CompletedOrderItem getCompletedOrderItems(int id) throws NoSuchElementException {

        Iterable<CompletedOrderItem> completedOrders = cOrdersRepository.findAll();

        for (CompletedOrderItem cItem : completedOrders) {
            if (id == cItem.getProductId()) {
               return cItem; 
            }
        }
        throw new NoSuchElementException();
    }

    public LinkedList<Order> getOrders() {

        Iterable<Order> all = orderRepository.findAll();
        LinkedList<Order> order = new LinkedList<Order>();
        for (Order item : all) {
            if (item.isDelivered() == false) {
                order.add(item);
            }
        }

        return order;
    }

    public void takeOrderOff(int id) throws NoSuchElementException{
        Order order = orderRepository.findById(id).orElseThrow();

        Iterable<OrderItem> all = orderItemsRepository.findAll();
        LinkedList<CompletedOrderItem> completed = new LinkedList<CompletedOrderItem>();
        CompletedOrderItem orderItem;

        for (OrderItem item : all) {
            if (item.getOrderNum() == id) {
                try {
                    orderItem = this.getCompletedOrderItems(item.getProductId());
                } catch (NoSuchElementException e) {
                    orderItem = new CompletedOrderItem();
                    orderItem.setName(item.getName());
                    orderItem.setProductId(item.getProductId());
                }
                ProductUnit productUnit = productsRepository.findById(item.getProductId()).orElseThrow();
                orderItem.setAmount(orderItem.getAmount() + item.getAmount());
                orderItem.setRating(productUnit.getRating());
                orderItem.setPrice(productUnit.getPrice() * orderItem.getAmount());
                completed.add(orderItem);
            }
        }
        for (CompletedOrderItem item : completed) {
            cOrdersRepository.save(item);
        }
        order.setDelivered(true);
        orderRepository.save(order);
    }

    public void deleteCompletedOrder(int id) {
        Order order = orderRepository.findById(id).orElseThrow();
        
        if (order.isDelivered()) {
            orderRepository.deleteById(id);
            Iterable<OrderItem> all = orderItemsRepository.findAll();
            for (OrderItem item : all) {
                if (item.getOrderNum() == id) {
                    orderItemsRepository.deleteById(item.getId());
                }
            }
        }
    }

    public void deleteShoppingCartOrders(int userId) {
        Iterable<ShoppingCart> all = shoppingCartService.getUsersProducts(userId);
        for (ShoppingCart item : all) {
            shoppingCartService.deleteById(item.getId());
        }
    }
}
