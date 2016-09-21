package com.jebstern.petrolbook.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Refill extends RealmObject {

    @PrimaryKey
    private long id;

    private String date;
    private String address;
    private String station;
    private double litres;
    private double cost;
    private String price;

    public Refill(String date, String address, String station, double litres, double cost, String price) {
        this.date = date;
        this.address = address;
        this.station = station;
        this.litres = litres;
        this.cost = cost;
        this.price = price;
    }

    public Refill() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public double getLitres() {
        return litres;
    }

    public void setLitres(double litres) {
        this.litres = litres;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
