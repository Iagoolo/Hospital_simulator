package model;

public abstract class Pessoa {
    
    private String cpf;
    private String nome;
    
    public Pessoa(){
        // construtor default
    }
    
    public Pessoa (String nome, String cpf){
        this.cpf = cpf;
        this.nome = nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
}
