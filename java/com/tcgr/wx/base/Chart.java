package com.tcgr.wx.base;

/**
 * Cartas AIS-WEB
 * Created by thomas on 28/04/16.
 */
public class Chart {

//    private String emenda;
//    private String especie;
    private String tipo;
    private String descricao;
    private String nomeCarta;
    private String icao;
    private String data;
    private String url;

//    public String getEmenda() {
//        return emenda;
//    }
//
//    public void setEmenda(String emenda) {
//        this.emenda = emenda;
//    }
//
//    public String getEspecie() {
//        return especie;
//    }
//
//    public void setEspecie(String especie) {
//        this.especie = especie;
//    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = "[" + tipo + "]";
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeCarta() {
        return nomeCarta;
    }

    public void setNomeCarta(String nomeCarta) {
        this.nomeCarta = nomeCarta;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
       this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}