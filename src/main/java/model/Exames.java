package model;

import java.util.Date;

public class Exames {
    
    private int idExames;
    private int idConsulta;
    private String tipo;
    private Date solicitadoEm;
    private String resultado;
    private Date dataResultado;
    private String status;

    public Exames(){
        // Construtor padrão
    }
    
    public Exames (int idExame, int idConsulta, String tipo, Date solicitacaoEm, String resultado, Date dataResultado, String status){
        this.idExames = idExame;
        this.idConsulta = idConsulta;
        this.tipo = tipo;
        this.solicitadoEm = solicitacaoEm;
        this.resultado = resultado;
        this.dataResultado = dataResultado;
        this.status = status;
    }

     public int getIdExames() {
        return idExames;
    }

    public void setIdExames(int idExames) {
        this.idExames = idExames;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public Date getSolicitadoEm() {
        return solicitadoEm;
    }

    public void setSolicitadoEm(Date solicitadoEm) {
        this.solicitadoEm = solicitadoEm;
    }
    
     public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Date getDataResultado() {
        return dataResultado;
    }

    public void setDataResultado(Date dataResultado) {
        this.dataResultado = dataResultado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }    
}
