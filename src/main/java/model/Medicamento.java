package model;

public class Medicamento {
    private int idMedicamento;
    private String nome;
    private String formula;
    private String forma;
    private String viaAdministracao;

    public Medicamento() {}

    public Medicamento(int idMedicamento, String nome, String formula, String forma, String viaAdministracao) {
        this.idMedicamento = idMedicamento;
        this.nome = nome;
        this.formula = formula;
        this.forma = forma;
        this.viaAdministracao = viaAdministracao;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getViaAdministracao() {
        return viaAdministracao;
    }

    public void setViaAdministracao(String viaAdministracao) {
        this.viaAdministracao = viaAdministracao;
    }
}