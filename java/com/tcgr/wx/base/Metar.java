package com.tcgr.wx.base;

/**
 * Created by thomas on 25/11/15.
 *
 */
public class Metar extends Weather {

    private String flightCategory;
    private String metarType;
    //private String skyCondition;
    private String wxString;

    public String getFlightCategory() {
        return flightCategory;
    }

    public void setFlightCategory(String flightCategory) {
        this.flightCategory = flightCategory;
    }

    public String getMetarType() {
        return metarType;
    }

    public void setMetarType(String metarType) {
        this.metarType = metarType;
    }

    /*public String getSkyCondition() {
        return skyCondition;
    }

    public void setSkyCondition(String skyCondition) {
        this.skyCondition = skyCondition;
    }*/

    public String getWxString() {
        return wxString;
    }

    public void setWxString(String wxString) {
        this.wxString = wxString;
    }

}