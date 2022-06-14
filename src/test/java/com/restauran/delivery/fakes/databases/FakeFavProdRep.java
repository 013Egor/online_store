package com.restauran.delivery.fakes.databases;

import java.util.LinkedList;
import java.util.Optional;


import com.restauran.delivery.entity.FavouriteProduct;
import com.restauran.delivery.repositories.FavouriteProductRepository;

public class FakeFavProdRep implements FavouriteProductRepository {

    LinkedList<FavouriteProduct> dataBase = new LinkedList<FavouriteProduct>();

    @Override
    public long count() {
        
        return dataBase.size();
    }

    @Override
    public void delete(FavouriteProduct entity) {
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
    public void deleteAll(Iterable<? extends FavouriteProduct> entities) {
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
        for (FavouriteProduct favouriteProduct : dataBase) {
             if (favouriteProduct.getId() == id) {
                return true;
             }
        }
        return false;
    }

    @Override
    public Iterable<FavouriteProduct> findAll() {
        LinkedList<FavouriteProduct> temp = new LinkedList<FavouriteProduct>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Iterable<FavouriteProduct> findAllById(Iterable<Integer> ids) {
  
        LinkedList<FavouriteProduct> temp = new LinkedList<FavouriteProduct>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Optional<FavouriteProduct> findById(Integer id) {
        
        return dataBase.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public <S extends FavouriteProduct> S save(S entity) {
        boolean isNew = true;
        for (FavouriteProduct item : dataBase) {
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
    public <S extends FavouriteProduct> Iterable<S> saveAll(Iterable<S> entities) {
        for (S s : entities) {
            dataBase.add(s);
        }
        return entities;
    }
    
}
