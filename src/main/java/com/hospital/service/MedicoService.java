package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.MedicoDAO;
import com.hospital.model.Medico;

public class MedicoService {
    
    private Connection connection;

    public MedicoService(Connection connection) {
        this.connection = connection;
    }

    public void cadastrarMedico(Medico medico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            medicoDAO.add(medico);
            connection.commit();

            System.out.println("Médico cadastrado com sucesso!!");
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar médico. Revertendo situação");

            try {
                connection.rollback();
            } catch(SQLException ex){
                System.err.println("Erro ao reverter a situação: " + ex.getMessage());
            }

            throw new SQLException("Error registering medico: " + e.getMessage(), e);
        }
    }

    public void atualizarMedico(Medico medico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);
        
        try {
            connection.setAutoCommit(false);
            medicoDAO.atualizar(medico);
            connection.commit();
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao atualizar médico", e);
        }
    }

    public void deletarMedico(String cpfMedico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            medicoDAO.deletar(cpfMedico);
            connection.commit();

            System.out.println("Médico deletado com sucesso!");
        } catch(SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar médico.", e);
        }
    }

    public Medico buscarMedicoCpf(String cpfMedico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);
        return medicoDAO.buscarPorCpf(cpfMedico);
    }

    public List<Medico> listarTodosMedicos() throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);
        return medicoDAO.listarTodos();
    }
}