package com.tcgr.wx.parser;

import com.tcgr.wx.base.Metar;
import com.tcgr.wx.parser.basic.BasicParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * Created by thomas on 25/11/15.
 * @see BasicParser
 */
public class WxMetarParser extends BasicParser<ArrayList<Metar>> {

    private Metar metar;
    private ArrayList<Metar> wx;

    @Override
    public int getSize() {
        return (wx == null) ? 0 : wx.size();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if(qName.equals("data")) {
            int arrSize = Integer.parseInt(attributes.getValue("num_results"));
            if(arrSize > 0) {
                wx = new ArrayList<>();
            }
        }

        if (qName.equals("METAR")) {
            metar = new Metar();
        }

        content = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {
            case "raw_text":
                metar.setRawText(getContent());
                break;

            case "station_id":
                metar.setStationId(getContent());
                break;

            case "flight_category":
                metar.setFlightCategory(getContent());
                break;

            case "elevation_m":
                metar.setElevation(Float.valueOf(getContent()));
                break;

            case "metar_type":
                metar.setMetarType(getContent());
                break;

            /*case "sky_condition":
                arrMetar[i].setSkyCondition(getContent());
                break;*/

            case "wx_string":
                metar.setWxString(getContent());
                break;

            case "METAR":
                wx.add(metar);
                break;
        }
    }

    @Override
    public ArrayList<Metar> getList() {
        return wx;
    }
}