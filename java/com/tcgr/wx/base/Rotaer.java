package com.tcgr.wx.base;

/**
 * ROTAER
 */
public class Rotaer {

    private String aeroCode;
    private String aeroName;
    private String cidade;
    private String uf;
    private String fir;
    private String tipoAero;
    private String pista;
    private String cmb;
    private String rffs;
    private String met;
    private String com;
    private String rdonav;
    private String ais;
    private String rmk;
    private String urlCartaVfr;
    private int size;
    private int altMetros;
    private int altPes;
    private boolean isComplete;

    //05/03/16 - O AisWeb retirou a informação de LAT/LON do XML resultante.
    /**
    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    private String lat;
    private String lon;
    */

    public void setAeroCode(String aeroCode) {
        this.aeroCode = aeroCode;
    }

    public String getUrlCartaVfr() {
        return urlCartaVfr;
    }

    public void setAeroName(String aeroName) {
        this.aeroName = aeroName;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public void setFir(String fir) {
        this.fir = fir;
    }

    public void setTipoAero(String tipoAero) {
        this.tipoAero = tipoAero;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public void setCmb(String cmb) {
        this.cmb = cmb;
    }

    public void setRffs(String rffs) {
        this.rffs = rffs;
    }

    public void setMet(String met) {
        this.met = met;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public void setRdonav(String rdonav) {
        this.rdonav = rdonav;
    }

    public void setAis(String ais) {
        this.ais = ais;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public void setUrlCartaVfr(@SuppressWarnings("UnusedParameters") String urlCartaVfr) {
        //TODO - URL temporaria, enquanto o AIS-WEB nao resolve o problema do URL atual inexistente.
        //TODO - this.urlCartaVfr = urlCartaVfr; (original)
        this.urlCartaVfr = "http://static.aisweb.decea.gov.br/assets/_img/vfr/" + aeroCode + ".jpg";
    }

    public void setAltMetros(int altMetros) {
        this.altMetros = altMetros;
    }

    public void setAltPes(int altPes) {
        this.altPes = altPes;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    /**
     * Monta os dados que serão exibidos na TextView
     *
     * @return String de dados em formato html.
     */
    public String getData() {

        if(aeroCode == null) {
            return null;
        }

        if(aeroName.equals("AD CNL")) {
            return "<b>Extrato do AIP (" + aeroCode + ")</b><br>" +
                    "<br><b>AD CNL</b>" +
                    "<br><b>AD CNL</b>" +
                    "<br><b>FIR</b> - " + fir;
        }

        String data = "<b>Extrato do AIP (" + aeroCode + ")</b><br>" +
                "<br><b>" + cidade + " (" + uf + ")</b>" +
                "<br><b>" + aeroName + "</b>" +
                //"<br>" + lat + " " + lon +
                "<br><b>FIR</b> - " + fir;

        if (isComplete) {
            data = data + "<br><b>Elevação</b> - " + altMetros + "m (" + altPes + "ft)" +
                    "<br>" + tipoAero +
                    "<br>" + pista +
                    "<br><b>CMB</b> - " + cmb + " RFFS - " + rffs +
                    "<br><b>MET</b> - " + met +
                    "<br><b>COM</b> - " + com +
                    "<br><b>RDONAV</b> - " + rdonav +
                    "<br><b>AIS</b> - " + ais +
                    "<br><b>RMK</b><br>" + rmk;
        }

        size = data.length();
        return data;
    }

    public int getSize() {
        return size;
    }
}