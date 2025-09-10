package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.hospital.dao.HistoricoMedicoDAO;
import com.hospital.model.HistoricoMedico;

public class HistoricoMedicoService {
    private Connection connection;

    public HistoricoMedicoService(Connection connection){
        this.connection = connection;
    }

    public HistoricoMedico cadastrarHistorico(HistoricoMedico historico) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO = new HistoricoMedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            historicoMedicoDAO.add(historico);
            connection.commit();

            System.out.println("Histórico médico cadastrado com sucesso!");
            return historico;
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao cadastrar histórico: " + e.getMessage());
        }
    }

    public void deletarHistorico(String cpfPaciente) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO = new HistoricoMedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            historicoMedicoDAO.deletar(cpfPaciente);
            connection.commit();

            System.out.println("Histórico deletado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar histórico: " + e.getMessage());
        }
    }

    public void atualizarHistorico(int idHistorico, String observacoes) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO = new HistoricoMedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            historicoMedicoDAO.atualizarObservacoes(idHistorico, observacoes);
            connection.commit();

            System.out.println("Histórico atualizado com sucesso");
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Erro ao atualizar histórico: " + e.getMessage());
        }
    }

    public HistoricoMedico buscarHistorico(String cpf) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO =  new HistoricoMedicoDAO(connection);
        return historicoMedicoDAO.buscarHistoricoPorPaciente(cpf);
    }
}
