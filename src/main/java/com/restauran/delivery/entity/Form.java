package com.restauran.delivery.entity;

public class Form {

    String username;
    String password;
    String passwordConfirm;
    String newPassword;
    String firstName;
    String lastName;
    String middleName;
    String telNumber;
    String email;
    String address;
    
    public Form() {}

    public Form(String firstName, String lastName, String middleName, 
            String telNumber, String email, String address) {
                
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.telNumber = telNumber;
        this.email = email;
        this.address = address;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
