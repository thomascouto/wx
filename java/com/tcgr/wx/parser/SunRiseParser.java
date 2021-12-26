package com.tcgr.wx.parser;

import com.tcgr.wx.base.SunRise;
import com.tcgr.wx.parser.basic.BasicParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * Pôr do Sol - Parser
 * @see BasicParser
 */
public class SunRiseParser extends BasicParser<ArrayList<SunRise>> {

    private ArrayList<SunRise> sunRiseArrayList = new ArrayList<>();
    private SunRise sunRise;

    public ArrayList<SunRise> getList() {
        return sunRiseArrayList;
    }

    @Override
    public int getSize() {
        return sunRiseArrayList.size();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("day")) {
            sunRise = new SunRise();
        }
        content = new StringBuilder();
    }

    /**
     *
     * <day>
     *     <date>2015-09-06</date>
     *     <sunrise>09:13</sunrise>
     *     <sunset>21:06</sunset>
     *     <weekDay>7</weekDay>
     *     <aero>SBBR</aero>
     * </day>
     *
     * @param uri ...
     * @param localName ...
     * @param qName Tag
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {
            case "date":
                sunRise.setDayMonthYear(getContent());
                break;

            case "sunrise":
                sunRise.setSunRise(getContent());
                break;

            case "sunset":
                sunRise.setSunSet(getContent());
                break;

            case "weekDay":
                sunRise.setWeek(Integer.parseInt(getContent()));
                break;

            /*case "aero": <aero>ICAO</aero>
                break;*/

            case "day":
                sunRiseArrayList.add(sunRise);
                break;
        }
    }
}