package com.example.caua.mymeds.receita;

public class Proposes {
    private String idReceita;
    private String idProposta;
    private String idUsuario;
    private String valor;
    private String entrega;
    private String obs;

    public Proposes(String idReceita, String idProposta, String idUsuario, String valor, String entrega, String obs) {
        this.idReceita = idReceita;
        this.idProposta = idProposta;
        this.valor = valor;
        this.entrega = entrega;
        this.obs = obs;
        this.idUsuario = idUsuario;
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(String idReceita) {
        this.idReceita = idReceita;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public void setIdProposta(String idProposta) {
        this.idProposta = idProposta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }


}