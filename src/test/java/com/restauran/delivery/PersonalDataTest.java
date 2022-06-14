package com.restauran.delivery;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restauran.delivery.entity.Form;
import com.restauran.delivery.entity.PersonalData;

import org.junit.jupiter.api.Test;

public class PersonalDataTest {

    String addressAfter = "Perm";
    String emailAfter = "after@after.com";
    String firstNameAfter = "Misha";
    String lastNameAfter = "Snigirev";
    String middleNameAfter = "Misha";
    String telNumberAfter = "0088";

    String address = "SPB";
    String email = "test@test.com";
    String firstName = "Egor";
    String lastName = "Karnaukhov";
    String middleName = "Georgievich";
    String telNumber = "8800";
    
    PersonalData fullData;
    PersonalData emptyData;
    Form form;

    @Test
    void testNewData() {   
        emptyData = new PersonalData();
        fullData = new PersonalData(firstName, lastName, middleName,
                                    telNumber, email, address);
        form = new Form(firstNameAfter, lastNameAfter, middleNameAfter,
                        telNumberAfter, emailAfter, addressAfter); 

        emptyData.setNewData(form);
        fullData.setNewData(form);

        assertFalse(emptyData.getFirstName().equals(""));
        assertFalse(emptyData.getLastName().equals(""));
        assertFalse(emptyData.getMiddleName().equals(""));
        assertFalse(emptyData.getTelNumber().equals(""));
        assertFalse(emptyData.getEmail().equals(""));
        assertFalse(emptyData.getAddress().equals(""));

        assertTrue(fullData.getFirstName().equals(firstNameAfter));
        assertTrue(fullData.getLastName().equals(lastNameAfter));
        assertTrue(fullData.getMiddleName().equals(middleNameAfter));
        assertTrue(fullData.getTelNumber().equals(telNumberAfter));
        assertTrue(fullData.getEmail().equals(emailAfter));
        assertTrue(fullData.getAddress().equals(addressAfter));
    }
}
