package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.SalaDAO;
import com.hospital.model.Sala;

public class SalaService {
    private Connection connection;

    public SalaService (Connection connection){
        this.connection = connection;
    }

    public Sala cadastrarSala (Sala sala) throws SQLException{
        SalaDAO salaDAO =  new SalaDAO(connection);

        try {
            connection.setAutoCommit(false);
            salaDAO.add(sala);
            connection.commit();

            System.out.println("Sala cadastrada com sucesso");
            return sala;
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao cadastrar sala: " + e.getMessage());
        }
    }

    public void deletarSala (int idSala) throws SQLException, Exception {
        SalaDAO salaDAO = new SalaDAO(connection);

        if (salaDAO.isSalaEmUso(idSala)){
            throw new Exception("Não é possível deletar uma sala em uso");
        }

        try {
            connection.setAutoCommit(false);
            salaDAO.deletar(idSala);
            connection.commit();

            System.out.println("Sala deletada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao deletar sala: " + e.getMessage());
        }
    }

    public void atualizarSala(Sala sala) throws SQLException{
        SalaDAO salaDAO = new SalaDAO(connection);

        try {
            connection.setAutoCommit(false);
            salaDAO.atualizar(sala);
            connection.commit();

            System.out.println("Sala atualizada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao atualizar sala: " + e.getMessage());
        }
    }

    public List<Sala> listarTodasSalas() throws SQLException{
        SalaDAO salaDAO = new SalaDAO(connection);
        return salaDAO.listar();
    }

    public Sala buscarSala(int idSala) throws SQLException{
        SalaDAO salaDAO = new SalaDAO(connection);
        return salaDAO.buscarSala(idSala);
    }
}