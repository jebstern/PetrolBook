package com.jebstern.petrolbook.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewestRefuelResponse {

    @SerializedName("hasRefuel")
    @Expose
    private Boolean hasRefuel;
    @SerializedName("object")
    @Expose
    private RefuelResponse refuelResponse;

    /**
     *
     * @return
     * The hasRefuel
     */
    public Boolean getHasRefuel() {
        return hasRefuel;
    }

    /**
     *
     * @param hasRefuel
     * The hasRefuel
     */
    public void setHasRefuel(Boolean hasRefuel) {
        this.hasRefuel = hasRefuel;
    }

    /**
     *
     * @return
     * The refuelResponse
     */
    public RefuelResponse getRefuelResponse() {
        return refuelResponse;
    }

    /**
     *
     * @param refuelResponse
     * The refuelResponse
     */
    public void setRefuelResponse(RefuelResponse refuelResponse) {
        this.refuelResponse = refuelResponse;
    }

}
