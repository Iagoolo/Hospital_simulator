package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.hospital.dao.AtendimentoDAO;
import com.hospital.exception.FilaVaziaException;
import com.hospital.model.Atendimento;

public class AtendimentoService {
    private Connection connection;

    public AtendimentoService(Connection connection){
        this.connection = connection;
    }

    public Atendimento realizarAtendimento(Atendimento atendimento) throws SQLException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);

        try {
            connection.setAutoCommit(false);
            atendimentoDAO.add(atendimento);
            connection.commit();

            System.out.println("Atendimento realizado com sucesso!");

            return atendimento;
        } catch (SQLException e) {
            connection.rollback();

            throw new SQLException("Erro ao realizar atendimento: " + e.getMessage());
        }
    }

    public void atualizarAtendimentos(int idAtendimento, int idSala, String status) throws SQLException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);

        try {
            connection.setAutoCommit(false);
            atendimentoDAO.atualizarSalaEStatus(idAtendimento, idSala, status);
            connection.commit();
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao realizar atualização de atendimento: " + e.getMessage());
        }
    }

    public void finalizarAtendimento(int idAtendimento) throws SQLException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);

        try {
            connection.setAutoCommit(false);
            atendimentoDAO.finalizarAtendimento(idAtendimento);
            connection.commit();

            System.out.println("Atendimento finalizado com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao finalizar atendimento: " + e.getMessage());
        }
    }
    
    public Atendimento buscarProximoPaciente() throws SQLException, FilaVaziaException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);
        Atendimento proximo = atendimentoDAO.buscarProximoPaciente();

        if (proximo == null){
            throw new FilaVaziaException("Não há pacientes aguardando na fila de triagem.");
        }

        return proximo;
    }
}
