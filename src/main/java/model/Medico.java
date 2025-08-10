package model;

public class Medico {
    
    private String cpfMedico;
    private String turno;

    public Medico() {
        // Default constructor
    }   

    public Medico(String cpfMedico, String turno) {
        this.cpfMedico = cpfMedico;
        this.turno = turno;
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
