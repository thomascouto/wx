package com.tcgr.wx.base;

/**
 * Created by thomas on 25/11/15.
 *
 *
 */
public abstract class Weather {

    private String rawText;
    private String stationId;
    private int elevation;

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = Math.round(elevation * 3.28f); //convertendo para pés (ft)
    }
}
