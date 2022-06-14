package com.restauran.delivery.service;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.FavouriteProduct;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.repositories.FavouriteProductRepository;
import com.restauran.delivery.repositories.ProductsRepository;

@Service
public class FavouriteProductService {

    
    FavouriteProductRepository favProdRepository;

    
    ProductsRepository productsRepository;
    
    public FavouriteProductService(FavouriteProductRepository favProdRepository,
            ProductsRepository productsRepository) {
        this.favProdRepository = favProdRepository;
        this.productsRepository = productsRepository;
    }

    public LinkedList<ProductUnit> getFavProducts(int userId) {

        Iterable<FavouriteProduct> fav = favProdRepository.findAll();
        LinkedList<ProductUnit> products = new LinkedList<ProductUnit>();
        ProductUnit temp;

        for (FavouriteProduct fProduct : fav) {
            try {
                if (userId == fProduct.getUserId()) {
                    temp = productsRepository.findById(fProduct.getProductId()).orElseThrow();
                    products.add(temp);
                }
            } catch (NoSuchElementException e) {
                favProdRepository.deleteById(fProduct.getId());
            }
            
        }

        return products;
    }

    public void save(FavouriteProduct product) {
        favProdRepository.save(product);
    }

    public void deleteById(int productId, int userId) {
        Iterable<FavouriteProduct> all = favProdRepository.findAll();
        Integer favId = null;

        for (FavouriteProduct item : all) {
            if (item.getProductId() == productId && item.getUserId() == userId) {
                favId = item.getId();
                break;
            }
        }
        if (favId != null) {
            favProdRepository.deleteById(favId);
        }
    }

    public void clearUsersFavours(int userId) {
        Iterable<FavouriteProduct> all = favProdRepository.findAll();
        
        for (FavouriteProduct item : all) {
            if (item.getUserId() == userId) {
                favProdRepository.deleteById(item.getId());
            }
        }
    }

    public boolean isFavourite(int userId, int productId) {

        Iterable<FavouriteProduct> all = favProdRepository.findAll();
        for (FavouriteProduct item : all) {
            if (item.getUserId() == userId && item.getProductId() == productId) {
                return true;
            }
        }

        return false;
    }
}
