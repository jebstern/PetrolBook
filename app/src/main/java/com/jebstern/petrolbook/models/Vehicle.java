package com.jebstern.petrolbook.models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Vehicle extends RealmObject {

    @PrimaryKey
    private int id;

    private String licenseNumber;
    private String model;
    private String petrolType;

    public Vehicle() {
    }

    public Vehicle(int id, String licenseNumber, String model, String petrolType) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.petrolType = petrolType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPetrolType() {
        return petrolType;
    }

    public void setPetrolType(String petrolType) {
        this.petrolType = petrolType;
    }
}
