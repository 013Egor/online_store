package com.restauran.delivery.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.restauran.delivery.entity.PersonalData;
import com.restauran.delivery.fakes.databases.FakePersonalData;

public class PersonalDataServiceTest {

    FakePersonalData dataRep = new FakePersonalData();
    PersonalDataService service = new PersonalDataService(dataRep);

    String address = "SPB";
    String email = "test@test.com";
    String firstName = "Egor";
    String lastName = "Karnaukhov";
    String middleName = "Georgievich";
    String telNumber = "8800";

    String addressAfter = "Perm";
    String emailAfter = "after@after.com";
    String firstNameAfter = "Misha";
    String lastNameAfter = "Snigirev";
    String middleNameAfter = "Misha";
    String telNumberAfter = "0088";

    @Test
    void testDeleteById() {
        PersonalData data = new PersonalData(firstName, lastName, 
                                            middleName, telNumber, email, address);

        dataRep.save(data);
        
        assertTrue(dataRep.existsById(data.getId()));

        service.deleteById(data.getId());

        assertFalse(dataRep.existsById(data.getId()));
    }

    @Test
    void testGetPersonalData() {
        PersonalData data = new PersonalData(firstName, lastName, 
                                            middleName, telNumber, email, address);

        PersonalData data2 = new PersonalData(firstNameAfter, lastNameAfter, 
                            middleNameAfter, telNumberAfter, emailAfter, addressAfter);
        dataRep.save(data);
        dataRep.save(data2);

        PersonalData personalData = service.getPersonalData(0);
        assertTrue(personalData.getId() == 0);

        assertThrowsExactly(NoSuchElementException.class, ()->{service.getPersonalData(100);});
    }
}
