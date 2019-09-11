package com.williantaiguara.anota.model;

import java.io.Serializable;

public class Falta implements Serializable {

    private String data;
    private String qtd;

    public Falta(){

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }
}
