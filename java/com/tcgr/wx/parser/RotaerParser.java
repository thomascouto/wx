package com.tcgr.wx.parser;

import com.tcgr.wx.base.Rotaer;
import com.tcgr.wx.parser.basic.BasicParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rotaer Parser
 *
 *
 *
 * <aisweb>
 * <aero rotaer="1" total="1">
 * <AeroCode>SBBR</AeroCode>
 * <name>PRES. JUSCELINO KUBITSCHEK</name>
 * <city>BRASILIA</city>
 * <uf>DF</uf>
 * <lat>-15.871111111111</lat>
 * <lng>-47.918611111111</lng>
 * <fir>SBBS</fir>
 * <rotaer>
 * <alt_m>1066</alt_m>
 * <alt_p>3497</alt_p>
 * <caract><![CDATA[INTL PUB/MIL 8SW UTC-3 VFR IFR L21, 23, 26 INFRAERO]]></caract>
 * <pista><![CDATA[<b>11R - L9(1), 12A- (3300x45 ASPH 68/F/B/W/T L14A, 15) -L9(1), 12A - 29L<br>11L - L4, 9(1), 12, 13- (3200x45 ASPH (9) 76/F/B/X/T L14, 15, 17) -L2A, 9(1), 12 - 29R<br>11 - (34.5x34.5 ASPH 20,0t) - 29</b><b><br></b>]]></pista>
 * <cmb><![CDATA[(7) PF, TF]]></cmb>
 * <rffs><![CDATA[CAT-9]]></rffs>
 * <met><![CDATA[(61) 3364-8556 e 3365-1769 CMA (5 a 11) (61) 3364-8724 CMM (1 a 12)]]></met>
 * <com><![CDATA[<i>TORRE 118.100 118.450 121.500 (3) SOLO 121.800 OPERAÇÕES (14) 122.500<br>TRÁFEGO (2) 121.000 ATIS 127.800 RÁDIO Vide BRASÍLIA/FIR<br>METEORO Vide BRASÍLIA/FIR</i>]]></com>
 * <rdonav><![CDATA[ILS/DME 29L IBS (12) 111.5 1552.85S/04756.55W<br>ILS 11L IBR (4) 110.3 1551.68S/04753.70W<br>LOC/DME 11R IDF (11) 109.9 1552.71S/04754.41W<br>ILS/DME 29R IND (8) 109.3 1551.82S/04755.78W<br>VOR/DME BSI 116.3 1552.31S/04801.32W<br>VOR/DME KUB 113.8 1552.85S/04756.77W<br>NDB (LM) IR 280 1551.85S/04756.24W]]></rdonav>
 * <ais><![CDATA[(6) (61) 3364-8550 e 3365-1855.]]></ais>
 * <rmk><![CDATA[(*) a. OBS VAC para entrada ou saída do circuito de tráfego.<br>b. TWY HOTEL CLR LDG/DEP eventuais. Vide capítulo II.<br>c. PRB FLT local no circuito de TFC treinamento de PROC IFR de TGL ou demais<br>tipos de treinamento que interfiram MOV AD de 1000-1430, 2000-0000.<br>d. Área de estacionamento operando conforme seguintes procedimentos:<br>(d.1) Obrigatório estacionamento aviação geral de pequeno porte até 5900Kg<br>(13007 libras) no pátio 06. Eventualmente será permitido o uso do pátio 04 quando<br>aquele estiver lotado;<br>(d.2) ACFT aviação geral com destino ao pátio 06 deverão utilizar obrigatoriamente<br>TWY do pátio 05. No período noturno OPR com cautela devido pátio iluminado,<br>porém sem balizamento;<br>(d.3) Via frontal aos hangares da aviação geral não consiste TWY, operação diurna<br>e noturna fica por conta e risco do operador.<br>e. Operação com cautela para o estacionamento ou “PUSH-BACK” de ACFT nas<br>entradas/saídas das posições 7 e 8 do pátio principal, devido à convergência da<br>sinalização horizontal naquelas posições, com impossibilidade de movimentação<br>simultânea de ou para as duas posições.<br>f. Não autorizado a apresentação de notificação de voo por radiotelefonia.<br>g. OBS voos de pássaros na RWY de LDG/TKOF, principalmente entre a THR 11L e<br>a TWY CHARLIE.<br>h. ACFT porte superior FK-50 deverá efetuar teste de motores na INT TWY ALFA<br>com TWY HOTEL ou TWY BRAVO com TWY HOTEL, COOR TWR - Brasília.<br>i. ACFT ASA FIXA que necessitem girar motores em marcha lenta para cheque de<br>EQPT e aferição de bússula deverão fazê-lo na INT TWY KILO com TWY MIKE,<br>sem restrições de horário, em coordenação TWR-Brasília e administração SBBR.<br>j. ACFT com envergadura acima de 20M e com ACN inferior a 10, com destino aos<br>Hangares de Aviação Geral para serviços exclusivamente de MAINT, AUTH o<br>acesso pela TWY QUEBEC QUEBEC, rebocada e estacionada totalmente no<br>interior do hangar, é compulsório acompanhamento do fiscal da INFRAERO.<br>k. A TWR BRASÍLIA não informará a hora de decolagem às ACFT. A instrução,<br>quanto a freqüência do próximo órgão a ser chamado após a decolagem e, se<br>necessárias, instruções complementares, serão emitidas juntamente com a<br>autorização de decolagem.<br>l. Nas operações de pouso, os pilotos não reportarão para TWR BRASÍLIA a<br>condição de trem de pouso, exceto nas situações de emergência com referência<br>ao seu baixamento e/ou travamento.<br>m. ACFT ASA ROTATIVA deverão fazer CTC prévio com administração do SBBR e<br>TWR - Brasília para cheque e teste de Motores.<br>n. TWY QUEBEC QUEBEC com início na TWY QUEBEC compulsório contato com a<br>TWR info PSN da ACFT. Permitindo Taxi de ACFT com envergadura até 20M.<br>o. ACFT TKOF AD situado na projeção dos limites laterais da TMA Brasília deverão<br>apresentar BFR TKOF PVC a qualquer Sala AIS credenciada da FIR de origem do<br>voo ou PVS à Sala AIS de Brasília pelo TEL: (61) 3365-1896.<br>p. ACFT procedentes de AD desprovidos de Sala AIS deverão apresentar BFR<br>TKOF PVC a qualquer Sala AIS credenciada da FIR de origem do FLT.<br>q. TWY Lima 4, Lima 5, Lima 6, Lima 8, Quebec e Romeo BTN Lima 3 e Lima 7<br>envergadura MAX 36M.<br>r. Somente poderão OPR ACFT que possam MNTN velocidade FNA MNM de 120KT<br>IAS, nos horários BTN 1100-1400 2100-0000.<br>s. Posições ACFT 42,43,45 E 46 AVBL somente HJ.<br>t. Cartas ver AIP-MAP.<br>(1) MEHT: a. RWY 11L - 72FT.<br>b. RWY 29R - 69FT.<br>c. RWY 29L - 60FT.<br>d. RWY 11R - 65FT.<br>(2) 0900-0100.<br>(3) EMERG.<br>(4) Front course NO AVBL acima do FL070.<br>]]></rmk>
 * <vfr>http://www.aisweb.aer.mil.br/arquivos/rotaer/vfr/SBBR.jpg</vfr>
 * </rotaer>
 * </aero>
 * </aisweb>
 */
public class RotaerParser extends BasicParser<String> {

    private Rotaer rotaer;

    public String getUrlCartaVfr() {
        return rotaer.getUrlCartaVfr();
    }

    @Override
    public String getList() {
        return rotaer.getData();
    }

    @Override
    public int getSize() {
        return rotaer.getSize();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equals("aero")) {
            rotaer = new Rotaer();
            int hasRotaer = Integer.parseInt(attributes.getValue("rotaer"));
            if (hasRotaer == 0) {
                rotaer.setIsComplete(false);
            } else {
                rotaer.setIsComplete(true);
            }
        }
        content = new StringBuilder();
    }

    /**
     * @param coord coordenada em formato decimal
     * @param isLon é longitude ?
     * @return String formatada D°M′S″
     */
    /**private String decimalToDMS(float coord, boolean isLon) {
        String output, degrees, minutes, seconds;

        // gets the modulus the coordinate divided by one (MOD1).
        // in other words gets all the numbers after the decimal point.
        // e.g. mod := -79.982195 % 1 == 0.982195
        //
        // next get the integer part of the coord. On other words the whole number part.
        // e.g. intPart := -79

        float mod = coord % 1;
        int intPart = (int) coord;

        //set degrees to the value of intPart
        //e.g. degrees := "-79"

        degrees = String.valueOf(Math.abs(intPart));

        // next times the MOD1 of degrees by 60 so we can find the integer part for minutes.
        // get the MOD1 of the new coord to find the numbers after the decimal point.
        // e.g. coord :=  0.982195 * 60 == 58.9317
        //	mod   := 58.9317 % 1    == 0.9317
        //
        // next get the value of the integer part of the coord.
        // e.g. intPart := 58

        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;
        if (intPart < 0) {
            // Convert number to positive if it's negative.
            intPart *= -1;
        }

        // set minutes to the value of intPart.
        // e.g. minutes = "58"
        minutes = String.valueOf(intPart);

        //do the same again for minutes
        //e.g. coord := 0.9317 * 60 == 55.902
        //e.g. intPart := 55
        coord = mod * 60;
        intPart = (int) coord;
        if (intPart < 0) {
            // Convert number to positive if it's negative.
            intPart *= -1;
        }

        // set seconds to the value of intPart.
        // e.g. seconds = "55"
        seconds = String.valueOf(intPart);

        // I used this format for android but you can change it
        // to return in whatever format you like
        // e.g. output = "-79/1,58/1,56/1"
        //output = degrees + "/1," + minutes + "/1," + seconds + "/1";

        //Standard output of D°M′S″

        char direction;
        if (isLon) {
            if (coord < 0) {
                direction = 'W';
            } else {
                direction = 'E';
            }
        } else {
            if (coord < 0) {
                direction = 'S';
            } else {
                direction = 'N';
            }
        }

        output = degrees + "° " + minutes + "' " + seconds + "\" " + direction;
        return output;
    }*/

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (qName) {
            case "AeroCode":
                rotaer.setAeroCode(getContent());
                break;
            case "name":
                rotaer.setAeroName(getContent());
                break;
            case "city":
                rotaer.setCidade(getContent());
                break;
            case "uf":
                rotaer.setUf(getContent());
                break;
//            case "lat":
//
//                try {
//                    String lat = decimalToDMS(Float.parseFloat(getContent()), false);
//                    rotaer.setLat(lat);
//                } catch (NumberFormatException ignored) {}
//
//                break;
//            case "lng":
//                try {
//                    String lon = decimalToDMS(Float.parseFloat(getContent()), true);
//                    rotaer.setLon(lon);
//                } catch (NumberFormatException ignored) {}
//
//                break;
            case "fir":
                rotaer.setFir(getContent());
                break;
            case "alt_m":
                rotaer.setAltMetros(Integer.parseInt(getContent()));
                break;
            case "alt_p":
                rotaer.setAltPes(Integer.parseInt(getContent()));
                break;
            case "caract":
                rotaer.setTipoAero(getContent());
                break;
            case "pista":
                rotaer.setPista(getContent());
                break;
            case "cmb":
                rotaer.setCmb(getContent());
                break;
            case "rffs":
                rotaer.setRffs(getContent());
                break;
            case "met":
                rotaer.setMet(getContent());
                break;
            case "com":
                rotaer.setCom(getContent());
                break;
            case "rdonav":
                rotaer.setRdonav(getContent());
                break;
            case "ais":
                rotaer.setAis(getContent());
                break;
            case "rmk":
                rotaer.setRmk(getContent());
                break;
            case "vfr":
                rotaer.setUrlCartaVfr(getContent());
                break;
        }
    }
}