package com.tcgr.wx.parser;

import com.tcgr.wx.base.Notam;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.parser.basic.BasicNotamParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Created by thomas on 22/02/16.
 *
 * <p/>
 * <p/>
 * PADRAO FAA...
 * <p/>
 * I0598/11 NOTAMR I0582/11
 * <p/>
 * Q) SBRE/QRRCA/IV/BO/W/000/030/0403S03841W008
 * A) SBFZ
 * B) 1106301914  C) PERM
 * E) SBR-267 RESTRICTED AREA ACT FLW CHARACTERISTICS:
 * 1- LATERAL BDRY: 0355.47S/03837.63W, 0356.29S/03834.71W,
 * 0411.34S/03840.89W, 0408.68S/03846.22W.
 * 2- VER BDRY: GND/3000FT
 * 3- TYPE OF RESTRICTION: PARAGLIDING FLT AND FLY WING
 * 4- ACT: PERM UNDER VMC
 * REF: AIP ENR 5.1.2
 * AIP ENR 6.1 L2 ENRC
 * AIP AD 2.24
 * F) GND  G) 3000FT AMSL
 * <p/>
 *
 * <NOTAM id="I0128/16">
 * <ItemQ>SBRE/QPICH/I/NBO /A /000/999/0347S03832W</ItemQ>
 * <ItemA>SBFZ</ItemA>
 * <ItemB>1602191458</ItemB>
 * <ItemC>1605182359</ItemC>
 * <ItemD/>
 * <ItemE>IAP VOR RWY 13 CHANGED:
 * 1- TO CIRCLING:
 * CAT     MDA          CEILING         VIS
 * A       990FT        1000FT         2000M
 * B       990FT        1000FT         2000M
 * C       990FT        1000FT         4400M
 * 2- STRAIGHT-IN:
 * CAT    VIS
 * A     2000M
 * B     2000M
 * C     4300M</ItemE>
 * </NOTAM>
 */
public class NotamIntlParser extends BasicNotamParser {

    private String icaoCode;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        switch (qName) {
            case  "NOTAMSET":
                icaoCode = attributes.getValue("ICAO");
                break;

            case "NOTAM":
                notam = new Notam(icaoCode);
                notam.setNotamCode(attributes.getValue("id"));
                break;
        }

        content = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {
            case "ItemQ":
                /**
                 * Neste caso, será <ItemQ>SAEF/QMRLC/IV/NBO /A /000/999/3449S05832W</ItemQ>
                 */
                notam.setCidade(getContent());
                break;

            case "ItemB": //<ItemB>1603161735</ItemB>
                String date = Util.formatNotamDate(getContent());
                notam.setDtInicio(date);
                break;

            case "ItemC": //<ItemC>1603192035</ItemC>
                String dateFinal = getContent();
                if (!dateFinal.equals("PERM")) {
                    dateFinal = Util.formatNotamDate(dateFinal);
                }
                notam.setDtTermino(dateFinal);
                break;

            case "ItemD": //<ItemD>16 19 1735-2035, 17 18 0400-0800 1735-2035</ItemD>
                notam.setDly(getContent());
                break;

            case "ItemE": //<ItemE>RWY 11/29 CLSD WIP MAINT</ItemE>
                notam.setContent(getContent().trim());
                break;

            case "NOTAM":
                notamArrayList.add(notam);
                break;
        }
    }
}