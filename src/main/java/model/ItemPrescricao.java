package model;

public class ItemPrescricao {
    private int idItem;
    private int idMedicamento;
    private String nomeMedicamento;
    private String dosagem;
    private String frequencia;
    private String duracao;
    private String instrucoes;

    public ItemPrescricao() {}

    public ItemPrescricao(int idItem, int idMedicamento, String nomeMedicamento, String dosagem, String frequencia, String duracao, String instrucoes) {
        this.idItem = idItem;
        this.idMedicamento = idMedicamento;
        this.nomeMedicamento = nomeMedicamento;
        this.dosagem = dosagem;
        this.frequencia = frequencia;
        this.duracao = duracao;
        this.instrucoes = instrucoes;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
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

    public String getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(String instrucoes) {
        this.instrucoes = instrucoes;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }
}