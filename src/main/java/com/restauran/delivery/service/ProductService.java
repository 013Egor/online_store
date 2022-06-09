package com.restauran.delivery.service;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.repositories.ProductsRepository;

@Service
public class ProductService {

    @Autowired
    ProductsRepository productsRepository;
    
    public void setNewRating(int id, int rating) throws NoSuchElementException {

        ProductUnit product = productsRepository.findById(id).orElseThrow();

        product.changeRating(rating);
        productsRepository.save(product);
    }

    public ProductUnit getProductById(int id) {
        
        return productsRepository.findById(id).orElseThrow();
    }

    public LinkedList<ProductUnit> getBestProducts() {
        Iterable<ProductUnit> all = productsRepository.findAll();
        LinkedList<ProductUnit> best = new LinkedList<ProductUnit>();

        for (ProductUnit item: all) {
            if (item.getRating() >= 4) {
                best.add(item);
            }
        }

        return best;
    }

    public Iterable<ProductUnit> getAll() {
        return productsRepository.findAll();
    }

    public boolean existsById(int id) {
        return productsRepository.existsById(id);
    }
}
