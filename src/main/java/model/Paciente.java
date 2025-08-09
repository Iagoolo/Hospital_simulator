package model;

public class Paciente {
    
    private String nome;
    private String nomePai;
    private String nomeMae;
    private String endereco;
    private String cpf;
    private int id;
    
    public Paciente() {
        // Default constructor
    }
    
    public Paciente(String nome, String nomePai, String nomeMae, String endereco, String cpf){
        this.nome = nome;
        this.nomePai = nomePai;
        this.nomeMae = nomeMae;
        this.endereco = endereco;
        this.cpf = cpf;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNomeMae() {
        return nomeMae;
    }
    
    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getNomePai() {
        return nomePai;
    }
    
    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
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
