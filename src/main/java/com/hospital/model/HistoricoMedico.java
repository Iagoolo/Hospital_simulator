package com.hospital.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HistoricoMedico {
    
    private int idHistorico;
    private String cpfPaciente;
    private String observacoes;
    private Date ultimaAtualizacao;
    private String statusHistorico;
    private List<Exames> exames;

    public HistoricoMedico(){
        // Construtor padrão
    }

    public HistoricoMedico (int idHistorico, String cpfPaciente, String observacoes, Date ultimaAtualizacao, String statusHistorico, List<Exames> exames){
        this.idHistorico = idHistorico;
        this.cpfPaciente = cpfPaciente;
        this.observacoes = observacoes;
        this.ultimaAtualizacao = ultimaAtualizacao;
        this.statusHistorico = statusHistorico;
        this.exames = exames;
    }

    // Getters and Setters
    public int getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(int idHistorico) {
        this.idHistorico = idHistorico;
    }
    
    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

      public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Date getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(Date ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getStatusHistorico() {
        return statusHistorico;
    }

    public void setStatusHistorico(String statusHistorico) {
        this.statusHistorico = statusHistorico;
    }

    public List<Exames> getExames() {
        return exames;
    }

    public void setExames(List<Exames> exames) {
        this.exames = exames;
    }

    public void addExame(Exames exame) {
        if (this.exames == null) {
            this.exames = new ArrayList<>();
        }
        
        this.exames.add(exame);
    }
}
