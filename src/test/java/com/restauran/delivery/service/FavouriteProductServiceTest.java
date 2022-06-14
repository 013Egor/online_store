package com.restauran.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.restauran.delivery.entity.FavouriteProduct;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.fakes.databases.FakeFavProdRep;
import com.restauran.delivery.fakes.databases.FakeProductRep;

public class FavouriteProductServiceTest {

    @Test
    void testClearUsersFavours() {
        FakeFavProdRep favProdRep = new FakeFavProdRep();
        FakeProductRep productRep = new FakeProductRep();
        FavouriteProductService service = 
                new FavouriteProductService(favProdRep, productRep);

        FavouriteProduct one = new FavouriteProduct(1, 1);
        FavouriteProduct two = new FavouriteProduct(2, 1);
        FavouriteProduct three = new FavouriteProduct(1, 2);
        
        service.save(one);
        service.save(two);
        service.save(three);

        service.clearUsersFavours(1);

        assertFalse(favProdRep.existsById(0));
        assertFalse(favProdRep.existsById(1));
        assertTrue(favProdRep.existsById(2));
    }

    @Test
    void testDeleteById() {
        FakeFavProdRep favProdRep = new FakeFavProdRep();
        FakeProductRep productRep = new FakeProductRep();
        FavouriteProductService service = 
                new FavouriteProductService(favProdRep, productRep);

        FavouriteProduct one = new FavouriteProduct(1, 1);
        FavouriteProduct two = new FavouriteProduct(2, 1);
        FavouriteProduct three = new FavouriteProduct(1, 2);
        
        service.save(one);
        service.save(two);
        service.save(three);

        service.deleteById(2, 1);

        assertTrue(favProdRep.existsById(0));
        assertFalse(favProdRep.existsById(1));
        assertTrue(favProdRep.existsById(2));
    }

    @Test
    void testGetFavProducts() {
        FakeFavProdRep favProdRep = new FakeFavProdRep();
        FakeProductRep productRep = new FakeProductRep();
        FavouriteProductService service = 
                new FavouriteProductService(favProdRep, productRep);

        FavouriteProduct one = new FavouriteProduct(1, 1);
        FavouriteProduct two = new FavouriteProduct(2, 1);
        FavouriteProduct three = new FavouriteProduct(1, 2);
        
        service.save(one);
        service.save(two);
        service.save(three);

        LinkedList<ProductUnit> fav = service.getFavProducts(1);
        Iterable<FavouriteProduct> all = favProdRep.findAll();
        int expected = 0;
        for (FavouriteProduct favouriteProduct : all) {
            if (favouriteProduct.getUserId() == 1) {
                expected++;
            }
        }
        assertEquals(expected, fav.size());
    }

    @Test
    void testIsFavourite() {
        FakeFavProdRep favProdRep = new FakeFavProdRep();
        FakeProductRep productRep = new FakeProductRep();
        FavouriteProductService service = 
                new FavouriteProductService(favProdRep, productRep);

        FavouriteProduct one = new FavouriteProduct(1, 1);
        FavouriteProduct two = new FavouriteProduct(2, 1);
        
        service.save(one);
        service.save(two);

        assertTrue(service.isFavourite(1, 1));
        assertTrue(service.isFavourite(1, 2));
        assertFalse(service.isFavourite(1, 3));
    }
}
