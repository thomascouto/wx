package com.tcgr.wx.parser;

import com.tcgr.wx.base.Taf;
import com.tcgr.wx.parser.basic.BasicParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * Created by thomas on 02/12/15.
 * @see BasicParser
 */
public class WxTafParser extends BasicParser<ArrayList<Taf>> {

    private Taf taf;
    private ArrayList<Taf> wx;

    @Override
    public ArrayList<Taf> getList() {
        return wx;
    }

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

        if (qName.equals("TAF")) {
            taf = new Taf();
        }

        content = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {

            case "raw_text":
                taf.setRawText(getContent());
                break;

            case "station_id":
                taf.setStationId(getContent());
                break;

            case "elevation_m":
                taf.setElevation(Float.valueOf(getContent()));
                break;

            case "TAF":
                wx.add(taf);
                break;
        }
    }
}