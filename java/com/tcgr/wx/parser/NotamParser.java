package com.tcgr.wx.parser;

import com.tcgr.wx.base.Notam;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.parser.basic.BasicNotamParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NotamParser extends BasicNotamParser {

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equals("item")) {
            int id = Integer.parseInt(attributes.getValue("id"));
            notam = new Notam(id);
        }

        content = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {
            case "n": //<n>Z2970/2015</n>
                notam.setNotamCode(getContent());
                break;

            case "e": //<e>AIS MIL HR SER MON TIL THU 1100-1430 1600-2000 FRI 1100-1500. HOLO/R)</e>
                notam.setContent(getContent().trim());
                break;

            case "d": //<d>DLY 1035-1230 1325-1442 1901-2145</d>
                notam.setDly(getContent());
                break;

            case "b": //<b>1512011035</b>
                String date = Util.formatNotamDate(getContent());
                notam.setDtInicio(date);
                break;

            case "c": //<c>1512072145</c>
                String dateFinal = getContent();
                if (!dateFinal.equals("PERM")) {
                    dateFinal = Util.formatNotamDate(dateFinal);
                }
                notam.setDtTermino(dateFinal);
                break;

            case "aero": //<aero>ORLANDO BEZERRA DE MENEZES</aero>
                notam.setAero(getContent());
                break;

            case "cidade": //<cidade>JUAZEIRO DO NORTE</cidade>
                notam.setCidade(getContent());
                break;

            case "uf": // <uf>CE</uf>
                notam.setUf(getContent());
                break;

            case "loc": //<loc>SBFZ</loc>
                notam.setIcaoCode(getContent());
                break;

            case "item":
                notamArrayList.add(notam);
                break;
        }
    }
}