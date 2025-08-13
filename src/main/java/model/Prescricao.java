package model;

import java.util.ArrayList;
import java.util.List;

public class Prescricao {
    private int idPrescricao;
    private int idConsulta;
    private List<ItemPrescricao> itens;

    public Prescricao() {
        this.itens = new ArrayList<>();
    }

    public Prescricao(int idPrescricao, int idConsulta) {
        this.idPrescricao = idPrescricao;
        this.idConsulta = idConsulta;
        this.itens = new ArrayList<>();
    }

    public int getIdPrescricao() {
        return idPrescricao;
    }

    public void setIdPrescricao(int idPrescricao) {
        this.idPrescricao = idPrescricao;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public List<ItemPrescricao> getItens() {
        return itens;
    }

    public void setItens(List<ItemPrescricao> itens) {
        this.itens = itens;
    }

    public void addItem(ItemPrescricao item) {
        this.itens.add(item);
    }
}
