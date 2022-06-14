package com.restauran.delivery.interfaces;

import java.util.LinkedList;

import com.restauran.delivery.entity.CompletedOrderItem;
import com.restauran.delivery.entity.Order;
import com.restauran.delivery.entity.OrderItem;

public interface OrderManagerRead {

    public LinkedList<Order> getUsersOrders(int userId);
    
    public LinkedList<OrderItem> getOrdersItems(int id);
    
    public Iterable<CompletedOrderItem> getCompletedOrders();
    
    public LinkedList<Order> getOrders();
}
