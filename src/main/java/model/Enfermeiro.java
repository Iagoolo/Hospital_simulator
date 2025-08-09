package model;

public class Enfermeiro {

    private String cpf_enfermeiro;

    public Enfermeiro() {
        // Default constructor
    }

    public Enfermeiro(String cpf_enfermeiro) {
        this.cpf_enfermeiro = cpf_enfermeiro;
    }

    public String getCpfEnfermeiro() {
        return cpf_enfermeiro;
    }

    public void setCpfEnfermeiro(String cpf_enfermeiro) {
        this.cpf_enfermeiro = cpf_enfermeiro;
    }
}
