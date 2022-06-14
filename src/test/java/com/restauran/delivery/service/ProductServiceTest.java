package com.restauran.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.fakes.databases.FakeProductRep;

public class ProductServiceTest {

    FakeProductRep repository = new FakeProductRep();
    ProductService service = new ProductService(repository);

    public ProductServiceTest() {
        ProductUnit unit;
        for (int i = 0; i < 6; i++) {
            unit = new ProductUnit();
            unit.setRating(i);
            repository.save(unit);
        }
     }

    @Test
    void testGetBestProducts() {
        LinkedList<ProductUnit> best = service.getBestProducts();

        assertEquals(2, best.size());
        repository.deleteById(4);
        repository.deleteById(5);

        best = service.getBestProducts();
        assertEquals(0, best.size());
    }

    @Test
    void testSetNewRating() {
        ProductUnit productUnit = repository.findById(0).orElseThrow();
        double before = productUnit.getRating();
        service.setNewRating(0, 5);
        assertNotEquals(before, productUnit.getRating());
    }
}
