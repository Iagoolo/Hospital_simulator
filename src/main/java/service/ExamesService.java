package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.ExamesDAO;
import model.Exames;

public class ExamesService {
    private Connection connection;
    
    public ExamesService(Connection connection){
        this.connection = connection;
    }

    public Exames cadastrarExame(Exames exame) throws SQLException{
        ExamesDAO examesDAO = new ExamesDAO(connection);

        try {
            connection.setAutoCommit(false);
            examesDAO.add(exame);
            connection.commit();

            System.out.println("Exame cadastrado com sucesso!");
            return exame;
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao cadastrar exame: " + e.getMessage());
        }
    }

    public void deletarExame(int idExame) throws SQLException{
        ExamesDAO examesDAO = new ExamesDAO(connection);

        try {
            connection.setAutoCommit(false);
            examesDAO.deletarExame(idExame);
            connection.commit();

            System.out.println("Exame deletado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar exame: " + e.getMessage());
        }
    }

    public void atualizarExame(Exames exame) throws SQLException{
        ExamesDAO examesDAO = new ExamesDAO(connection);

        try {
            connection.setAutoCommit(false);
            examesDAO.update(exame);
            connection.commit();

            System.out.println("Exame atualizado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao atualizar exame: " + e.getMessage());
        }
    }

    public Exames buscarExame(int idExame) throws SQLException{
        ExamesDAO examesDAO = new ExamesDAO(connection);
        return examesDAO.buscar(idExame);
    }

    public List<Exames> listarTodosExames(int idConsulta) throws SQLException{
        ExamesDAO examesDAO = new ExamesDAO(connection);
        return examesDAO.listar(idConsulta);
    }
}
