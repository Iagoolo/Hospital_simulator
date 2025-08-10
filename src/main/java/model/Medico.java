package model;

public class Medico {
    
    private String nome;
    
    private String cpfMedico;
    private String turno;
    
    public Medico() {
        // Default constructor
    }   
    
    public Medico(String nome, String cpfMedico, String turno) {
        this.cpfMedico = cpfMedico;
        this.turno = turno;
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCpfMedico() {
        return cpfMedico;
    }

    public void setCpfMedico(String cpfMedico) {
        this.cpfMedico = cpfMedico;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
