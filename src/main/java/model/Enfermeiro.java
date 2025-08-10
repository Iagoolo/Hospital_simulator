package model;

public class Enfermeiro {

    private String cpfEnfermeiro;

    public Enfermeiro() {
        // Default constructor
    }

    public Enfermeiro(String cpfEnfermeiro) {
        this.cpfEnfermeiro = cpfEnfermeiro;
    }

    public String getCpfEnfermeiro() {
        return cpfEnfermeiro;
    }

    public void setCpfEnfermeiro(String cpfEnfermeiro) {
        this.cpfEnfermeiro = cpfEnfermeiro;
    }
}
