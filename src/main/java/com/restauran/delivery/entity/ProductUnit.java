package com.restauran.delivery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProductUnit {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id;

    String name;
    double rating;
    int votingAmout;
    String composition;
    String picture;
    int amount;
    double price;

    public ProductUnit() {
        name = "";
        composition = "";
        picture = "";
        amount = 0;
        rating = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void changeAmount(int amount) {
        this.amount += amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }
    
    public String getComposition() {
        return composition;
    }

    public String getPicture() {
        return picture;
    }
    
    public int getAmount() {
        return amount;
    }

    public String getPicturePath() {
        return "/img/" + id + "/" + picture;
    }

    public int getVotingAmout() {
        return votingAmout;
    }

    public void increaseVotingAmount(){
        votingAmout++;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
