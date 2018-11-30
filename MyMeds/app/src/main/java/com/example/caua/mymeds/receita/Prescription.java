package com.example.caua.mymeds.receita;

public class Prescription {
    private String receita;
    private String proposta;
    private String data;
    private String id;
    private String descricao;
    private String obs;
    private int numPropostas;

    public Prescription(String receita, String proposta, String data, String id, String descricao, String obs) {
        this.receita = receita;
        this.proposta = proposta;
        this.data = data;
        this.id = id;
        this.descricao = descricao;
        this.obs = obs;
    }

    public String getReceita() {
        return receita;
    }

    public void setReceita(String receita) {
        this.receita = receita;
    }

    public String getProposta() {
        return proposta;
    }

    public void setProposta(String proposta) {
        this.proposta = proposta;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getNumPropostas() {
        return numPropostas;
    }

    public void setNumPropostas(int numPropostas) {
        this.numPropostas = numPropostas;
    }

    @Override
    public String toString() {
        return "Receita " + this.getDescricao() + "\n Quantidade de Propostas: " + this.getProposta();
    }
}
