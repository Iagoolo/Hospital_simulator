package com.hospital.model;

import java.sql.Time;

public class Atendimento {
    
    private int idAtendimento;
    private Integer idConsulta;
    private Integer idTriagem;
    private Integer idSala;
    private String cpfPaciente;
    private Time horaAtendimento;
    private String status;
    private String senha;

    public Atendimento(){
        // Construtor padrão
    }

    public Atendimento(int idAtendimento, String senha, Integer idConsulta, Integer idTriagem, Integer idSala, Time horaAtendimento, String status, String cpfPaciente){
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

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Integer getIdTriagem() {
        return idTriagem;
    }

    public void setIdTriagem(Integer idTriagem) {
        this.idTriagem = idTriagem;
    }

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
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