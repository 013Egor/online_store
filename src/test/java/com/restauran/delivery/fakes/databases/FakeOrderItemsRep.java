package com.restauran.delivery.fakes.databases;

import java.util.LinkedList;
import java.util.Optional;

import com.restauran.delivery.entity.OrderItem;
import com.restauran.delivery.repositories.OrderItemsRepository;

public class FakeOrderItemsRep implements OrderItemsRepository {

    LinkedList<OrderItem> dataBase = new LinkedList<OrderItem>();

    @Override
    public long count() {
        
        return dataBase.size();
    }

    @Override
    public void delete(OrderItem entity) {
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
    public void deleteAll(Iterable<? extends OrderItem> entities) {
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
        for (OrderItem orderItem : dataBase) {
             if (orderItem.getId() == id) {
                return true;
             }
        }
        return false;
    }

    @Override
    public Iterable<OrderItem> findAll() {
        
        LinkedList<OrderItem> temp = new LinkedList<OrderItem>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Iterable<OrderItem> findAllById(Iterable<Integer> ids) {
  
        LinkedList<OrderItem> temp = new LinkedList<OrderItem>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Optional<OrderItem> findById(Integer id) {
        
        return dataBase.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public <S extends OrderItem> S save(S entity) {
        boolean isNew = true;
        for (OrderItem item : dataBase) {
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
    public <S extends OrderItem> Iterable<S> saveAll(Iterable<S> entities) {
        for (S s : entities) {
            dataBase.add(s);
        }
        return entities;
    }
    
}
