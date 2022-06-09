package com.restauran.delivery.service;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.FavouriteProduct;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.repositories.FavouriteProductRepository;
import com.restauran.delivery.repositories.ProductsRepository;

@Service
public class FavouriteProductService {

    @Autowired
    FavouriteProductRepository favProdRepository;

    @Autowired
    ProductsRepository productsRepository;
    
    public LinkedList<ProductUnit> getFavProducts(int userId) {

        Iterable<FavouriteProduct> fav = favProdRepository.findAll();
        LinkedList<ProductUnit> products = new LinkedList<ProductUnit>();
        ProductUnit temp;

        for (FavouriteProduct fProduct : fav) {
            if (userId == fProduct.getUserId()) {
                temp = productsRepository.findById(fProduct.getProductId()).orElseThrow();
                products.add(temp);
            }
        }

        return products;
    }

    public void save(FavouriteProduct product) {
        favProdRepository.save(product);
    }

    public void deleteById(int id) {
        Iterable<FavouriteProduct> all = favProdRepository.findAll();
        Integer favId = null;

        for (FavouriteProduct item : all) {
            if (item.getProductId() == id) {
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
