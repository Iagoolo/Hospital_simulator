package model;

public class Paciente extends Pessoa{
    
    public Paciente() {
        // Default constructor
    }

    public Paciente(String nome, String nomePai, String nomeMae, String endereco, String cpf, int idade){
        super(nome, cpf, nomePai, nomeMae, endereco, idade);
    }
}
