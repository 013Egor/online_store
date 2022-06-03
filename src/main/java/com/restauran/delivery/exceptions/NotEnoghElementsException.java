package com.restauran.delivery.exceptions;

public class NotEnoghElementsException extends ArithmeticException {
    
    String name;
    int amount;
    
    public NotEnoghElementsException(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
    
    public NotEnoghElementsException(String s, String name, int amount) {
        super(s);
        this.name = name;
        this.amount = amount;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
