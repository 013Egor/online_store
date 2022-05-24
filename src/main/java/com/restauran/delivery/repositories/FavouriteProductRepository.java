package com.restauran.delivery.repositories;

import com.restauran.delivery.entity.FavouriteProduct;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteProductRepository extends CrudRepository<FavouriteProduct, Integer>{
    
}
