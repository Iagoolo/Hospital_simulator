package com.hospital.model;

public class ItemPrescricao {
    private int idItem;
    private int idPrescricao;
    private int idMedicamento;
    private String dosagem;
    private String frequencia;
    private String duracao;
    private String observacoes;

    public ItemPrescricao() {}

    public ItemPrescricao(int idItem, int idPrescricao, int idMedicamento, String dosagem, String frequencia, String duracao, String observacoes) {
        this.idItem = idItem;
        this.idPrescricao = idPrescricao;
        this.idMedicamento = idMedicamento;
        this.dosagem = dosagem;
        this.frequencia = frequencia;
        this.duracao = duracao;
        this.observacoes = observacoes;
    }

    // Getters and Setters
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdPrescricao() {
        return idPrescricao;
    }

    public void setIdPrescricao(int idPrescricao) {
        this.idPrescricao = idPrescricao;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }
}