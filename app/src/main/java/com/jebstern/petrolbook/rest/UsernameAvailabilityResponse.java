package com.jebstern.petrolbook.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsernameAvailabilityResponse {

    @SerializedName("usernameExists")
    @Expose
    private Boolean usernameExists;

    /**
     *
     * @return
     * The usernameExists
     */
    public Boolean getUsernameExists() {
        return usernameExists;
    }

    /**
     *
     * @param usernameExists
     * The usernameExists
     */
    public void setUsernameExists(Boolean usernameExists) {
        this.usernameExists = usernameExists;
    }

}
