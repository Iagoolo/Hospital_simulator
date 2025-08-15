package model;

import java.sql.Time;

public class Chamada {
    
    private int idChamada;
    private String senha;
    private int idConsulta;
    private int idTriagem;
    private int idSala;
    private Time horaChamada;
    private String status;

    public Chamada(){
        // Construtor padrão
    }

    public Chamada(int idChamada, String senha, int idConsulta, int idTriagem, int idSala, Time horaChamada, String status){
        this.idChamada = idChamada;
        this.senha = senha;
        this.idConsulta = idConsulta;
        this.idTriagem = idTriagem;
        this.idSala = idSala;
        this.horaChamada = horaChamada;
        this.status = status;
    }
    
    public int getIdChamada() {
        return idChamada;
    }

    public void setIdChamada(int idChamada) {
        this.idChamada = idChamada;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdTriagem() {
        return idTriagem;
    }

    public void setIdTriagem(int idTriagem) {
        this.idTriagem = idTriagem;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public Time getHoraChamada() {
        return horaChamada;
    }

    public void setHoraChamada(Time horaChamada) {
        this.horaChamada = horaChamada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
