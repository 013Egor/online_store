package com.restauran.delivery;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restauran.delivery.entity.Form;
import com.restauran.delivery.entity.PersonalData;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@SpringBootTest
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

    @BeforeTestClass
    void setData() {

        emptyData = new PersonalData();
        fullData = new PersonalData(firstName, lastName, middleName,
                                    telNumber, email, address);
        form = new Form(firstNameAfter, lastNameAfter, middleNameAfter,
                        telNumberAfter, emailAfter, addressAfter);    
    }

    @Test
    void testNewData() {   

        emptyData.setNewData(form);
        fullData.setNewData(form);

        assertTrue(emptyData.getFirstName().equals(""));
        assertTrue(emptyData.getLastName().equals(""));
        assertTrue(emptyData.getMiddleName().equals(""));
        assertTrue(emptyData.getTelNumber().equals(""));
        assertTrue(emptyData.getEmail().equals(""));
        assertTrue(emptyData.getAddress().equals(""));

        assertTrue(fullData.getFirstName().equals(firstNameAfter));
        assertTrue(fullData.getLastName().equals(lastNameAfter));
        assertTrue(fullData.getMiddleName().equals(middleNameAfter));
        assertTrue(fullData.getTelNumber().equals(telNumberAfter));
        assertTrue(fullData.getEmail().equals(emailAfter));
        assertTrue(fullData.getAddress().equals(addressAfter));
    }
}
