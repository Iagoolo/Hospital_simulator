package model;

public class Medico {
    
    private String cpf_medico;
    private String turno;

    public Medico() {
        // Default constructor
    }   

    public Medico(String cpf_medico, String turno) {
        this.cpf_medico = cpf_medico;
        this.turno = turno;
    }
    
    public String getCpfMedico() {
        return cpf_medico;
    }

    public void setCpfMedico(String cpf_medico) {
        this.cpf_medico = cpf_medico;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
