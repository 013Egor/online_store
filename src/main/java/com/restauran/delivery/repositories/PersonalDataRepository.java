package com.restauran.delivery.repositories;

import com.restauran.delivery.entity.PersonalData;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDataRepository extends CrudRepository<PersonalData, Integer> {
    
}
