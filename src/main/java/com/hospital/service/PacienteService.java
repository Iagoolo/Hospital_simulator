package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.PacienteDAO;
import com.hospital.model.Paciente;

public class PacienteService {
    private Connection connection;

    public PacienteService(Connection connection){
        this.connection = connection;
    }

    public void cadastrarPaciente(Paciente paciente) throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);

        try{
            connection.setAutoCommit(false);
            pacienteDAO.add(paciente);
            connection.commit();

            System.out.println("Paciente cadastrado com sucesso!");
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar paciente. Revertendo situação.");

            try{
                connection.rollback();
            } catch (SQLException ex){
                System.err.println("Erro ao reverter situação: " + ex.getMessage());
            }

            throw new SQLException("Erro ao registrar paciente: " + e.getMessage());
        }
    }

    public void atualizar(Paciente paciente) throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);

        try {
            connection.setAutoCommit(false);
            pacienteDAO.atualizar(paciente);
            connection.commit();

            System.out.println("Paciente atualizado com sucesso");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar paciente. Revertendo situação.");

            try {
                connection.rollback();
            } catch (SQLException ex){
                System.err.println("Erro ao reverter situação: " + ex.getMessage());
            }

            throw new SQLException("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    public void deletarPaciente(String cpf) throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);

        try {
            connection.setAutoCommit(false);
            pacienteDAO.deletar(cpf);
            connection.commit();

            System.out.println("Paciente deletado com sucesso!");
        } catch (SQLException e){
            
            try{
                connection.rollback();
            } catch (SQLException ex){
                System.err.println("Erro ao reverter deletação de Paciente: " + ex.getMessage());
            }

            throw new SQLException("Erro ao deletar paciente: " + e.getMessage());
        }
    }

    public Paciente buscarPacienteCpf(String cpf) throws SQLException {
        PacienteDAO pacienteDAO = new PacienteDAO(connection);
        return pacienteDAO.buscarPorCpf(cpf);
    }

    public List<Paciente> listarTodosPacientes() throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);
        return pacienteDAO.listarTodos();
    }
}
