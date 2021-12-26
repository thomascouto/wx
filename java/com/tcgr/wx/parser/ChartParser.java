package com.tcgr.wx.parser;

import com.tcgr.wx.base.Chart;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.parser.basic.BasicParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * @see Chart
 * Created by thomas on 28/04/16.
 *
 * EXEMPLO:
 *
 * <aisweb>
 *     <cartas emenda="2016-04-28" lastupdate="{ts '2016-02-29 13:18:24'}" total="8">
 *         <item id="0abd6ea0-3de8-1030-95e7-72567f175e3a">
 *             <especie>Convencionais</especie>
 *             <tipo>ADC</tipo>
 *             <tipo_descr>Carta de Aeródromo</tipo_descr>
 *             <nome>ADC</nome
 *             <IcaoCode>SBJU</IcaoCode>
 *             <dt>2012-06-28</dt>
 *             <link>http://ais.decea.gov.br/download/?arquivo=0abd6ea0-3de8-1030-95e7-72567f175e3a&amp;apikey=1622901454</link>
 *         </item>
 *     </cartas>
 * </aisweb>
 *
 */
public class ChartParser extends BasicParser {

    private ArrayList<Chart> arrayList;
    private Chart carta;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        switch (qName) {
            case "cartas":
                int qtd = Integer.parseInt(attributes.getValue("total"));
                arrayList = new ArrayList<>(qtd);
                break;

            case "item":
                carta = new Chart();
                break;
        }

        content = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {
//            case "especie":
//                carta.setEspecie(getContent());
//                break;

            case "tipo":
                carta.setTipo(getContent());
                break;

            case "tipo_descr":
                carta.setDescricao(getContent());
                break;

            case "nome":
                carta.setNomeCarta(getContent());
                break;

            case "IcaoCode":
                carta.setIcao(getContent());
                break;

            case "dt":
                String data = getContent();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    Date date = sdf.parse(data);
                    data = new SimpleDateFormat("dd/MM/yyyy", Constants.BRAZIL).format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                carta.setData(data);
                break;

            case "link":
                carta.setUrl(getContent());
                break;

            case "item":
                arrayList.add(carta);
                break;

        }
    }

    @Override
    public ArrayList<Chart> getList() {
        return arrayList;
    }

    @Override
    public int getSize() {
        return arrayList.size();
    }
}
