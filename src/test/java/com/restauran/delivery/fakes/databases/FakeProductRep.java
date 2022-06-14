package com.restauran.delivery.fakes.databases;

import java.util.LinkedList;
import java.util.Optional;

import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.repositories.ProductsRepository;

public class FakeProductRep implements ProductsRepository {

    LinkedList<ProductUnit> dataBase = new LinkedList<ProductUnit>();

    @Override
    public long count() {
        
        return dataBase.size();
    }

    @Override
    public void delete(ProductUnit entity) {
        for (int i = 0; i < dataBase.size(); i++) {
            if (entity.getId() == dataBase.get(i).getId()) {
                dataBase.remove(i);
                break;
            }
        } 
        
    }

    @Override
    public void deleteAll() {
        dataBase.clear();
    }

    @Override
    public void deleteAll(Iterable<? extends ProductUnit> entities) {
        dataBase.clear();
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {
        dataBase.clear();
    }

    @Override
    public void deleteById(Integer id) {
        dataBase.removeIf(x -> x.getId() == id);
    }

    @Override
    public boolean existsById(Integer id) {
        for (ProductUnit product : dataBase) {
             if (product.getId() == id) {
                return true;
             }
        }
        return false;
    }

    @Override
    public Iterable<ProductUnit> findAll() {
        
        LinkedList<ProductUnit> temp = new LinkedList<ProductUnit>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Iterable<ProductUnit> findAllById(Iterable<Integer> ids) {
  
        LinkedList<ProductUnit> temp = new LinkedList<ProductUnit>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Optional<ProductUnit> findById(Integer id) {
        
        return dataBase.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public <S extends ProductUnit> S save(S entity) {
        boolean isNew = true;
        for (ProductUnit item : dataBase) {
            if (item.getId() == entity.getId()) {
                item.setAll(entity);
                isNew = false;
            }
        }
        if (isNew == true) {
            entity.setId(dataBase.size());
            dataBase.add(entity);
        }

        return entity;
    }

    @Override
    public <S extends ProductUnit> Iterable<S> saveAll(Iterable<S> entities) {
        for (S s : entities) {
            dataBase.add(s);
        }
        return entities;
    }
    
}
