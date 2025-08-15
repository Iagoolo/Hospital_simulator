package model;

import java.sql.Date;

public class HistoricoMedico {
    
    private int idHistorico;
    private String cpfPaciente;
    private String observacoes;
    private Date ultimaAtualizacao;
    private String status;

    public HistoricoMedico(){
        // Construtor padrão
    }

    public HistoricoMedico (int idHistorico, String cpfPaciente, String observacoes, Date ultimaAtualizacao, String status){
        this.idHistorico = idHistorico;
        this.cpfPaciente = cpfPaciente;
        this.observacoes = observacoes;
        this.ultimaAtualizacao = ultimaAtualizacao;
        this.status = status;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
