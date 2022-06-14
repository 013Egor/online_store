package com.restauran.delivery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FavouriteProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    int productId;
    int userId;

    public FavouriteProduct() {}
    
    public FavouriteProduct(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
    }
    
    public void setAll (FavouriteProduct product) {
        this.id = product.id.intValue();
        this.productId = product.productId;
        this.userId = product.userId;
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

    public void setId(Integer id) {
        this.id = id;
    }
}
