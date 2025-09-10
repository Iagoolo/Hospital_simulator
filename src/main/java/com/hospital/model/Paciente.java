package com.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Paciente extends Pessoa{

    List<String> sintomas;
    public Paciente() {
        // Default constructor
        sintomas = new ArrayList<>();
    }

    public Paciente(String nome, String nomePai, String nomeMae, String endereco, String cpf, int idade){
        super(nome, cpf, nomePai, nomeMae, endereco, idade);
        sintomas = new ArrayList<>();
    }

    public List<String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    public void addSintomas(String sintoma){
        sintomas.add(sintoma);
    }
}
