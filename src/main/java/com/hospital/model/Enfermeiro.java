package com.hospital.model;

public class Enfermeiro extends Pessoa {
    
    public Enfermeiro() {
        // Default constructor
    }

    public Enfermeiro(String cpfEnfermeiro, String nome, String nomePai, String nomeMae, String endereco, int idade) {
        super(nome, cpfEnfermeiro, nomePai, nomeMae, endereco, idade);
    }
}
