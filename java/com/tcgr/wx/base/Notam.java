package com.tcgr.wx.base;

import java.io.Serializable;

/**
 * NOTAM
 */
public class Notam implements Serializable {

    private static final long serialVersionUID = -4825125606595100083L;
    private int id;
    private String cidade;
    private String uf;
    private String aero;
    private String icaoCode;
    private String notamCode;
    private String content;
    private String dtInicio;
    private String dtTermino;
    private String dly;

    public Notam() {
    }

    public Notam(int id) {
        this.id = id;
    }

    public Notam(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public String getNotamCode() {
        return notamCode;
    }

    public void setNotamCode(String notamCode) {
        this.notamCode = notamCode;
    }

    //TODO - Verificar utilidade no futuro
// --Commented out by Inspection START (28/05/16 23:28):
//    public int getId() {
//        return id;
//    }
// --Commented out by Inspection STOP (28/05/16 23:28)

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValidade() {

        if(!dly.equals("")) {
            return dtInicio.concat(" - ").concat(dtTermino).concat("\n").concat(dly);
        }
        return dtInicio.concat(" - ").concat(dtTermino);
    }

    public void setDtInicio(String dtInicio) {
        this.dtInicio = dtInicio;
    }

    public void setDtTermino(String dtTermino) {
        this.dtTermino = dtTermino;
    }

    public void setDly(String dly) {
        this.dly = dly;
    }

    public void setAero(String aero) {
        this.aero = aero;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {

        if((uf == null) || (aero == null)) {
            return  cidade.concat("\n").concat(icaoCode);
        }

        if(!cidade.equals("")) {
            return icaoCode.concat(" - ").concat(cidade).concat(", ").concat(uf).concat("\n").concat(aero);
        } else {
            return aero;
        }
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }
}