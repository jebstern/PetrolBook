package com.jebstern.petrolbook.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateAccountResponse {

    @SerializedName("accountCreated")
    @Expose
    private Boolean accountCreated;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * @return The accountCreated
     */
    public Boolean getAccountCreated() {
        return accountCreated;
    }

    /**
     * @param accountCreated The accountCreated
     */
    public void setAccountCreated(Boolean accountCreated) {
        this.accountCreated = accountCreated;
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