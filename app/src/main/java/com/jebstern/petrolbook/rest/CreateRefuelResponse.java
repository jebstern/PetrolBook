package com.jebstern.petrolbook.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CreateRefuelResponse {

    @SerializedName("refuelCreated")
    @Expose
    private Boolean refuelCreated;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * @return The refuelCreated
     */
    public Boolean getRefuelCreated() {
        return refuelCreated;
    }

    /**
     * @param refuelCreated The refuelCreated
     */
    public void setRefuelCreated(Boolean refuelCreated) {
        this.refuelCreated = refuelCreated;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}