package com.restauran.delivery.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.NoSuchElementException;

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
    
    OrderRepository orderRepository;

    OrderItemsRepository orderItemsRepository;

    ShoppingCartService shoppingCartService;

    CompletedOrdersRepository cOrdersRepository;

    ProductsRepository productsRepository;

    public OrderManager(OrderRepository orderRepository, OrderItemsRepository orderItemsRepository,
            ShoppingCartService shoppingCartService, CompletedOrdersRepository cOrdersRepository,
            ProductsRepository productsRepository) {

        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.shoppingCartService = shoppingCartService;
        this.cOrdersRepository = cOrdersRepository;
        this.productsRepository = productsRepository;
    }

    public void order(int userId) {

        LinkedList<ShoppingCart> allUsers = shoppingCartService.getUsersProducts(userId);
        if (allUsers.size() == 0) {
            throw new NoSuchElementException();
        }
        
        GregorianCalendar cal = new GregorianCalendar();
        Order savedOrder = orderRepository.save(new Order(userId, cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
        LinkedList<OrderItem> ordersProducts = new LinkedList<OrderItem>();
        
        for (ShoppingCart item : allUsers) {
            if (productsRepository.existsById(item.getProductId()) == false) {
                orderRepository.deleteById(savedOrder.getId());
                deleteShoppingCartOrders(userId);
                throw new NoSuchElementException("This product is unavailable for order");
            }
            OrderItem order = new OrderItem(item.getName(), item.getProductId(), item.getAmount());
            order.setOrderNum(savedOrder.getId());
            order.setUserId(userId);
            ordersProducts.add(order);
            shoppingCartService.deleteById(item.getProductId(), userId);
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

    public LinkedList<OrderItem> getOrdersItems(int orderId) {

        Iterable<OrderItem> all = orderItemsRepository.findAll();
        LinkedList<OrderItem> products = new LinkedList<OrderItem>();
        
        for (OrderItem item: all) {
            if (item.getOrderNum() == orderId) {
                products.add(item);
            }
        }

        return products;
    }

    public Iterable<CompletedOrderItem> getCompletedOrders() {

        return cOrdersRepository.findAll();
    }

    private CompletedOrderItem getCompletedOrderItem(int productId) throws NoSuchElementException {

        Iterable<CompletedOrderItem> completedOrders = cOrdersRepository.findAll();

        for (CompletedOrderItem cItem : completedOrders) {
            if (productId == cItem.getProductId()) {
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

    public void takeOrderOff(int orderId) throws NoSuchElementException{
        Order order = orderRepository.findById(orderId).orElseThrow();

        Iterable<OrderItem> all = orderItemsRepository.findAll();
        LinkedList<CompletedOrderItem> completed = new LinkedList<CompletedOrderItem>();
        CompletedOrderItem orderItem;

        for (OrderItem item : all) {
            if (item.getOrderNum() == orderId) {
                try {
                    orderItem = this.getCompletedOrderItem(item.getProductId());
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

    public void deleteCompletedOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        
        if (order.isDelivered()) {
            orderRepository.deleteById(orderId);
            Iterable<OrderItem> all = orderItemsRepository.findAll();
            for (OrderItem item : all) {
                if (item.getOrderNum() == orderId) {
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
