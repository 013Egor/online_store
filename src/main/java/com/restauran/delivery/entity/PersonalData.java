package com.restauran.delivery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PersonalData {
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    Integer id;
    
    String firstName;
    String lastName;
    String middleName;
    String telNumber;
    String email;
    String address;
    int userId;

    public PersonalData() {}

    public PersonalData(String firstName, String lastName, String middleName, String telNumber, String email,
            String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.telNumber = telNumber;
        this.email = email;
        this.address = address;
    }

    public void setAll (PersonalData data) {
        this.id = data.id.intValue();
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.middleName = data.middleName;
        this.telNumber = data.telNumber;
        this.email = data.email;
        this.address = data.address;
        this.userId = data.userId;
    }

    public PersonalData(Form userForm) {
        this.setAddress(userForm.getAddress());
        this.setEmail(userForm.getEmail());
        this.setFirstName(userForm.getFirstName());
        this.setLastName(userForm.getLastName());
        this.setMiddleName(userForm.getMiddleName());
        this.setTelNumber(userForm.getTelNumber());
    }

    public void setNewData(Form userForm) {
        if (userForm.getFirstName().equals("") == false){
            this.firstName = userForm.getFirstName();
        }
        if (userForm.getLastName().equals("") == false) {
            this.lastName = userForm.getLastName();
        }
        if (userForm.getMiddleName().equals("") == false) { 
            this.middleName = userForm.getMiddleName();
        }
        if (userForm.getTelNumber().equals("") == false) { 
            this.telNumber = userForm.getTelNumber();
        }
        if (userForm.getEmail().equals("") == false) { 
            this.email = userForm.getEmail();
        }
        if (userForm.getAddress().equals("") == false) { 
            this.address = userForm.getAddress();
        }
    }

    public Integer getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getTelNumber() {
        return telNumber;
    }
    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
