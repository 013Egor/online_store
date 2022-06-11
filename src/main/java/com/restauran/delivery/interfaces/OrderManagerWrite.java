package com.restauran.delivery.interfaces;

public interface OrderManagerWrite {

    public void order(int userId);
    
    public void clearOrders(int userId);
    
    public void takeOrderOff(int id);
}
