package model;

public class Medico extends Pessoa {
    
    private String turno;
    
    public Medico() {
        // Default constructor
    }   
    public Medico(String nome, String cpf, String nomePai, String nomeMae, String endereco, String turno, int idade) {
        super(nome, cpf, nomePai, nomeMae, endereco, idade);
        this.turno = turno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
