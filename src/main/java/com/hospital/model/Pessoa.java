package com.hospital.model;

public abstract class Pessoa {
    
    private String cpf;
    private String nome;
    private String nomePai;
    private String nomeMae;
    private String endereco;
    private int idade;

    public Pessoa(){
        // construtor default
    }

    public Pessoa (String nome, String cpf, String nomePai, String nomeMae, String endereco, int idade){
        this.cpf = cpf;
        this.nome = nome;
        this.nomePai = nomePai;
        this.nomeMae = nomeMae;
        this.endereco = endereco;
        this.idade = idade;
    }
    
    // Getters and Setters
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

    public String getNomePai() {
        return nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public String getEndereco() {
        return endereco;
    }
    
    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}
