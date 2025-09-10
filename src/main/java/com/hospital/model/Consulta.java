package com.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    
    private int idConsulta;
    private int idTriagem;
    private int sala;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private String observacao;
    private String diagnostico;
    private String cpfPaciente;
    private String cpfMedico;
    private Prescricao prescricao;

    public Consulta() {
        // Default constructor
    }

    public Consulta(int idConsulta, int idTriagem, int sala, LocalDate dataConsulta, LocalTime horaConsulta, String observacao, String diagnostico, String cpfPaciente, String cpfMedico) {
        this.idConsulta = idConsulta;
        this.idTriagem = idTriagem;
        this.sala = sala;
        this.dataConsulta = dataConsulta;
        this.horaConsulta = horaConsulta;
        this.observacao = observacao;
        this.diagnostico = diagnostico;
        this.cpfPaciente = cpfPaciente;
        this.cpfMedico = cpfMedico;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public int getIdTriagem() {
        return idTriagem;
    }

    public int getSala() {
        return sala;
    }

    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    public LocalTime getHoraConsulta() {
        return horaConsulta;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public String getCpfMedico() {
        return cpfMedico;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public void setIdTriagem(int idTriagem) {
        this.idTriagem = idTriagem;
    }

    public void setSala(int sala) {
        this.sala = sala;
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public void setHoraConsulta(LocalTime horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    public void setCpfMedico(String cpfMedico) {
        this.cpfMedico = cpfMedico;
    }

    public Prescricao getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(Prescricao prescricao) {
        this.prescricao = prescricao;
    }
}
