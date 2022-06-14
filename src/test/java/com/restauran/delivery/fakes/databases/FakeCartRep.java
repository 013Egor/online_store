package com.restauran.delivery.fakes.databases;

import java.util.LinkedList;
import java.util.Optional;

import com.restauran.delivery.entity.ShoppingCart;
import com.restauran.delivery.repositories.ShoppingCartRepository;

public class FakeCartRep implements ShoppingCartRepository {

    LinkedList<ShoppingCart> dataBase = new LinkedList<ShoppingCart>();

    @Override
    public long count() {
        
        return dataBase.size();
    }

    @Override
    public void delete(ShoppingCart entity) {
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
    public void deleteAll(Iterable<? extends ShoppingCart> entities) {
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
        for (ShoppingCart shoppingCart : dataBase) {
             if (shoppingCart.getId() == id) {
                return true;
             }
        }
        return false;
    }

    @Override
    public Iterable<ShoppingCart> findAll() {
        LinkedList<ShoppingCart> temp = new LinkedList<ShoppingCart>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Iterable<ShoppingCart> findAllById(Iterable<Integer> ids) {
  
        LinkedList<ShoppingCart> temp = new LinkedList<ShoppingCart>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Optional<ShoppingCart> findById(Integer id) {
        
        return dataBase.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public <S extends ShoppingCart> S save(S entity) {
        boolean isNew = true;
        for (ShoppingCart cart : dataBase) {
            if (cart.getId() == entity.getId()) {
                cart.setAll(entity);
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
    public <S extends ShoppingCart> Iterable<S> saveAll(Iterable<S> entities) {
        for (S s : entities) {
            dataBase.add(s);
        }
        return entities;
    }
    
}
