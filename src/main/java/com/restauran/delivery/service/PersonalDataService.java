package com.restauran.delivery.service;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.PersonalData;
import com.restauran.delivery.repositories.PersonalDataRepository;

@Service
public class PersonalDataService {
    
    
    PersonalDataRepository pDataRepository;

    public PersonalDataService(PersonalDataRepository pDataRepository) {
        this.pDataRepository = pDataRepository;
    }

    public PersonalData getPersonalData(int id) {

        Iterable<PersonalData> temp = pDataRepository.findAll();
        Iterator<PersonalData> it = temp.iterator();
        PersonalData data = null;

        while (it.hasNext()) {
            data = it.next();
            if (data.getUserId() == id) {
                return data;
            }
        }

        throw new NoSuchElementException("This user is not existed");
    }

    public void save(PersonalData data) {
        pDataRepository.save(data);
    }

    public void deleteById(int id) {
        Iterable<PersonalData> all = pDataRepository.findAll();
        
        for (PersonalData item : all) {
            if (item.getUserId() == id) {
                pDataRepository.deleteById(item.getId());
            }
        }
    }
}
