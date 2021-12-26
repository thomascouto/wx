package com.tcgr.wx.base;

import com.tcgr.wx.core.interfaces.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Classe Pôr do Sol / Nascer do Sol para aeroportos Brasileiros.
 */
public class SunRise {

    private String dayMonth;
    private String year;
    private String sunRise;
    private String sunSet;
    private int week;

    /**
     * Formato aaaa-mm-dd
     *
     * Converte para duas Strings: dd/mm e aaaa.format(aisWebFormat.parse(dayMonthYear));
     *
     * @param dayMonthYear ...
     */
    public void setDayMonthYear(String dayMonthYear) {

        SimpleDateFormat aisWebFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            dayMonth = new SimpleDateFormat("dd/MM", Constants.BRAZIL).format(aisWebFormat.parse(dayMonthYear));
            year = new SimpleDateFormat("yyyy", Constants.BRAZIL).format(aisWebFormat.parse(dayMonthYear));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDayMonth() {
        return dayMonth;
    }

    public String getYear() {
        return year;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }

    // TODO: 28/05/16 - Verificar se ha utilidade futuramente
    @SuppressWarnings("unused")
    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}