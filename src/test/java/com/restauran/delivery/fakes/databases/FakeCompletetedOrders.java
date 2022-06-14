package com.restauran.delivery.fakes.databases;

import java.util.LinkedList;
import java.util.Optional;

import com.restauran.delivery.entity.CompletedOrderItem;
import com.restauran.delivery.repositories.CompletedOrdersRepository;

public class FakeCompletetedOrders implements CompletedOrdersRepository {

    LinkedList<CompletedOrderItem> dataBase = new LinkedList<CompletedOrderItem>();

    @Override
    public long count() {
        
        return dataBase.size();
    }

    @Override
    public void delete(CompletedOrderItem entity) {
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
    public void deleteAll(Iterable<? extends CompletedOrderItem> entities) {
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
        for (CompletedOrderItem completedOrderItem : dataBase) {
             if (completedOrderItem.getId() == id) {
                return true;
             }
        }
        return false;
    }

    @Override
    public Iterable<CompletedOrderItem> findAll() {
        LinkedList<CompletedOrderItem> temp = new LinkedList<CompletedOrderItem>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Iterable<CompletedOrderItem> findAllById(Iterable<Integer> ids) {
  
        LinkedList<CompletedOrderItem> temp = new LinkedList<CompletedOrderItem>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Optional<CompletedOrderItem> findById(Integer id) {
        
        return dataBase.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public <S extends CompletedOrderItem> S save(S entity) {
        boolean isNew = true;
        for (CompletedOrderItem item : dataBase) {
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
    public <S extends CompletedOrderItem> Iterable<S> saveAll(Iterable<S> entities) {
        for (S s : entities) {
            dataBase.add(s);
        }
        return entities;
    }
    
}
