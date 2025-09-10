package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.hospital.dao.PrescricaoDAO;
import com.hospital.model.Prescricao;

public class PrescricaoService {
    private Connection connection;

    public PrescricaoService(Connection connection){
        this.connection = connection;
    }

    public void criarPrescricao(Prescricao prescricao) throws SQLException{
        PrescricaoDAO prescricaoDAO = new PrescricaoDAO(connection);

        try{
            connection.setAutoCommit(false);
            prescricaoDAO.add(prescricao);
            connection.commit();

            System.out.println("Prescrição criada com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao criar prescrição: " + e.getMessage());
        }
    }

    public void deletarPrescricao(int idPrescricao) throws SQLException{
        PrescricaoDAO prescricaoDAO = new PrescricaoDAO(connection);

        try {
            connection.setAutoCommit(false);
            prescricaoDAO.delete(idPrescricao);
            connection.commit();

            System.out.println("Prescrição deletada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao deletar prescrição: " + e.getMessage());
        }
    }

    public Prescricao buscaPrescricaoConsulta(int idConsulta) throws SQLException{
        PrescricaoDAO prescricaoDAO = new PrescricaoDAO(connection);
        return prescricaoDAO.getPrescricaosByConsulta(idConsulta);
    }
}
