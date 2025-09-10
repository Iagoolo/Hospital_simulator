package com.hospital.model;

import java.sql.Time;

public class Atendimento {
    
    private int idAtendimento;
    private int idConsulta;
    private int idTriagem;
    private int idSala;
    private String cpfPaciente;
    private Time horaAtendimento;
    private String status;
    private String senha;

    public Atendimento(){
        // Construtor padrão
    }

    public Atendimento(int idAtendimento, String senha, int idConsulta, int idTriagem, int idSala, Time horaAtendimento, String status, String cpfPaciente){
        this.idAtendimento = idAtendimento;
        this.senha = senha;
        this.idConsulta = idConsulta;
        this.idTriagem = idTriagem;
        this.idSala = idSala;
        this.horaAtendimento = horaAtendimento;
        this.status = status;
        this.cpfPaciente = cpfPaciente;
    }
    
    public int getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(int idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdTriagem() {
        return idTriagem;
    }

    public void setIdTriagem(int idTriagem) {
        this.idTriagem = idTriagem;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public Time getHoraAtendimento() {
        return horaAtendimento;
    }

    public void setHoraAtendimento(Time horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }
}