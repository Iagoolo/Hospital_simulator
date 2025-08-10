package model;

public class Medico extends Pessoa {
    
    private String turno;
    
    public Medico() {
        // Default constructor
    }   
    
    public Medico(String nome, String cpf, String turno) {
        super(nome, cpf);
        this.turno = turno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
