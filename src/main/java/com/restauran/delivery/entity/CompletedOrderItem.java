package com.restauran.delivery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CompletedOrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    int productId;
    String name;
    int amount;
    double rating;
    double price;

    public CompletedOrderItem() {}

    public void setAll (CompletedOrderItem item) {
        this.id = item.id.intValue();        
        this.productId = item.productId;
        this.name = item.name;
        this.amount = item.amount;
        this.rating = item.rating;
        this.price = item.price;
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
