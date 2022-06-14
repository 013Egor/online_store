package com.restauran.delivery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShoppingCart {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id;

    int productId;
    int userId;
    int amount;
    String name;
    
    public ShoppingCart() {}

    public ShoppingCart(String name, int productId, int userId, int amount) {
        this.name = name;
        this.productId = productId;
        this.userId = userId;
        this.amount = amount;
    }

    public void setAll(ShoppingCart c) {
        ShoppingCart cart = (ShoppingCart) c;
        this.name = cart.name;
        this.productId = cart.productId;
        this.userId = cart.userId;
        this.amount = cart.amount;
        this.id = cart.id;
    }

    public Integer getId() {
        return id;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
