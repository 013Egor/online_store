package com.restauran.delivery.fakes.databases;

import java.util.LinkedList;
import java.util.Optional;

import com.restauran.delivery.entity.PersonalData;
import com.restauran.delivery.repositories.PersonalDataRepository;

public class FakePersonalData implements PersonalDataRepository {

    LinkedList<PersonalData> dataBase = new LinkedList<PersonalData>();

    @Override
    public long count() {
        
        return dataBase.size();
    }

    @Override
    public void delete(PersonalData entity) {
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
    public void deleteAll(Iterable<? extends PersonalData> entities) {
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
        for (PersonalData PersonalData : dataBase) {
             if (PersonalData.getId() == id) {
                return true;
             }
        }
        return false;
    }

    @Override
    public Iterable<PersonalData> findAll() {
        LinkedList<PersonalData> temp = new LinkedList<PersonalData>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Iterable<PersonalData> findAllById(Iterable<Integer> ids) {
  
        LinkedList<PersonalData> temp = new LinkedList<PersonalData>();
        for (int i = 0; i < dataBase.size(); i++) {
            temp.add(dataBase.get(i));
        }
        return  temp;
    }

    @Override
    public Optional<PersonalData> findById(Integer id) {
        
        return dataBase.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public <S extends PersonalData> S save(S entity) {
        boolean isNew = true;
        for (PersonalData item : dataBase) {
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
    public <S extends PersonalData> Iterable<S> saveAll(Iterable<S> entities) {
        for (S s : entities) {
            dataBase.add(s);
        }
        return entities;
    }
    
}
