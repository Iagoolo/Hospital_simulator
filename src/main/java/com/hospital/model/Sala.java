package com.hospital.model;

public class Sala {
    
    private int idSala;
    private int andar;
    private String tipoSala;

    public Sala(){
        // Construtor padrão
    }

    public Sala(int idSala, int andar, String tipoSala){
        this.idSala = idSala;
        this.andar = andar;
        this.tipoSala = tipoSala;
    }
    
    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public int getAndar() {
        return andar;
    }

    public void setAndar(int andar) {
        this.andar = andar;
    }

    public String getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(String tipoSala) {
        this.tipoSala = tipoSala;
    }

}
