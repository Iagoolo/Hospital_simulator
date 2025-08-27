package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.EnfermeiroDAO;
import model.Enfermeiro;

public class EnfermeiroService {
    
    private Connection connection;

    public EnfermeiroService (Connection connection){
        this.connection = connection;
    }

    public void cadastrarEnfermeiro(Enfermeiro enfermeiro) throws SQLException{
        EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(connection);

        try {
            connection.setAutoCommit(false);
            enfermeiroDAO.add(enfermeiro);
            connection.commit();

            System.out.println("Enfermeiro cadastrado com sucesso");
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar enfermeiro. Revertendo situação");

            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao reverter a situação: " + ex.getMessage());
            }

            throw new SQLException("Erro ao registrar médico " + e.getMessage());
        }
    }

    public void atualizarEnfermeiro(Enfermeiro enfermeiro) throws SQLException{
        EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(connection);

        try {
            connection.setAutoCommit(false);
            enfermeiroDAO.atualizar(enfermeiro);
            connection.commit();

            System.out.println("Enfermeiro atualizado com sucesso");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao atualizar enfermeiro: " + e.getMessage());
        }
    }

    public void deletarEnfermeiro(String cpfEnfermeiro) throws SQLException{
        EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(connection);

        try {
            connection.setAutoCommit(false);
            enfermeiroDAO.deletar(cpfEnfermeiro);
            connection.commit();

            System.out.println("Enfermeiro atualizado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar médico: " + e.getMessage());
        }
    }

    public Enfermeiro buscarEnfermeiroCpf(String cpfEnfermeiro) throws SQLException{
        EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(connection);
        return enfermeiroDAO.buscarPorCpf(cpfEnfermeiro);
    }

    public List<Enfermeiro> listarTodosEnfermeiros() throws SQLException{
        EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(connection);
        return enfermeiroDAO.listarTodos();
    }
}
