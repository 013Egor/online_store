package com.restauran.delivery.repositories;

import com.restauran.delivery.entity.CompletedOrderItem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedOrdersRepository extends CrudRepository<CompletedOrderItem, Integer>{
    
}
