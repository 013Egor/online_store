package com.restauran.delivery.repositories;

import com.restauran.delivery.entity.OrderItem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends CrudRepository<OrderItem, Integer> {
    
}
