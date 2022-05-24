package com.restauran.delivery.repositories;

import com.restauran.delivery.entity.ShoppingCart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Integer>{
    
}
