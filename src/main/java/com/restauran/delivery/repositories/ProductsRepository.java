package com.restauran.delivery.repositories;

import com.restauran.delivery.entity.ProductUnit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends CrudRepository<ProductUnit, Integer> {
 
}
