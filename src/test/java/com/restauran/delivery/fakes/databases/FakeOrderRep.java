package com.restauran.delivery.fakes.databases;

import java.util.LinkedList;
import java.util.Optional;

import com.restauran.delivery.entity.Order;
import com.restauran.delivery.repositories.OrderRepository;

public class FakeOrderRep implements OrderRepository {

    LinkedList<Order> dataBase = new LinkedList<Order>();

    @Override
    public long count() {
        
        return dataBase.size();
    }

    @Override
    public void delete(Order entity) {
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
    public void deleteAll(Iterable<? extends Order> entities) {
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
        for (Order order : dataBase) {
             if (order.getId() == id) {
                return true;
             }
        }
        return false;
    }

    @Override
    public Iterable<Order> findAll() {
        
        LinkedList<Order> temp = new LinkedList<Order>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Iterable<Order> findAllById(Iterable<Integer> ids) {
  
        LinkedList<Order> temp = new LinkedList<Order>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Optional<Order> findById(Integer id) {
        
        return dataBase.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public <S extends Order> S save(S entity) {
        boolean isNew = true;
        for (Order item : dataBase) {
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
    public <S extends Order> Iterable<S> saveAll(Iterable<S> entities) {
        for (S s : entities) {
            dataBase.add(s);
        }
        return entities;
    }
    
}

