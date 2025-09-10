package com.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Triagem {
    
    private int idTriagem;
    private String prioridade;
    private LocalDate dataTriagem;
    private LocalTime horaTriagem;
    private double temperatura;
    private double peso;
    private String cpfPaciente;
    private String cpfEnfermeiro;
    
    public Triagem() {
        // Default constructor
    }

    public Triagem(int idTriagem, String prioridade, LocalDate dataTriagem, LocalTime horaTriagem, double temperatura, double peso, String cpfPaciente, String cpfEnfermeiro) {
        this.idTriagem = idTriagem;
        this.prioridade = prioridade;
        this.dataTriagem = dataTriagem;
        this.horaTriagem = horaTriagem;
        this.temperatura = temperatura;
        this.peso = peso;
        this.cpfPaciente = cpfPaciente;
        this.cpfEnfermeiro = cpfEnfermeiro;
    }

    public int getIdTriagem() {
        return idTriagem;
    }

    public void setIdTriagem(int idTriagem) {
        this.idTriagem = idTriagem;
    }

     public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getDataTriagem() {
        return dataTriagem;
    }

    public void setDataTriagem(LocalDate dataTriagem) {
        this.dataTriagem = dataTriagem;
    }
    
     public LocalTime getHoraTriagem() {
        return horaTriagem;
    }

    public void setHoraTriagem(LocalTime horaTriagem) {
        this.horaTriagem = horaTriagem;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    public String getCpfEnfermeiro() {
        return cpfEnfermeiro;
    }

    public void setCpfEnfermeiro(String cpfEnfermeiro) {
        this.cpfEnfermeiro = cpfEnfermeiro;
    }
}