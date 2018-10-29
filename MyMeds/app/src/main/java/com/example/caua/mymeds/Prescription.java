package com.example.caua.mymeds;

public class Prescription {
    private String receita;
    private String proposta;
    private int numPropostas;

    public Prescription(String receita, String proposta) {
        this.receita = receita;
        this.proposta = proposta;
    }

    @Override
    public String toString() {
        return "Receita: " + receita + " \nProposta: " + proposta;
    }
}
