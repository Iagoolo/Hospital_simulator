package model;

public class Enfermeiro extends Pessoa {
    
    public Enfermeiro() {
        // Default constructor
    }
    
    public Enfermeiro(String cpfEnfermeiro, String nome) {
        super(nome, cpfEnfermeiro);
    }
}
