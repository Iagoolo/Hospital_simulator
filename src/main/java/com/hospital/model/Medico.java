package com.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Medico extends Pessoa {
    
    private String turno;
    private List<String> especialidades;

    public Medico() {
        this.especialidades = new ArrayList<>();
    }

    public Medico(String nome, String cpf, String nomePai, String nomeMae, String endereco, String turno, int idade) {
        super(nome, cpf, nomePai, nomeMae, endereco, idade);
        this.turno = turno;
        this.especialidades = new ArrayList<>();
    }

    // Getters and Setters
    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public void addEspecialidade(String especialidade) {
        this.especialidades.add(especialidade);
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }
}
