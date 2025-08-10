package model;

public class Paciente extends Pessoa{
    
    private String nomePai;
    private String nomeMae;
    private String endereco;
    
    public Paciente() {
        // Default constructor
    }
    
    public Paciente(String nome, String nomePai, String nomeMae, String endereco, String cpf){
        super(nome, cpf);
        this.nomePai = nomePai;
        this.nomeMae = nomeMae;
        this.endereco = endereco;
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
}
