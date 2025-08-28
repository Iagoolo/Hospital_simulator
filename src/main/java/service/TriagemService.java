package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.TriagemDAO;
import model.Triagem;

public class TriagemService {
    private Connection connection;

    public TriagemService(Connection connection){
        this.connection = connection;
    }

    public Triagem realizarTriagem(Triagem triagem) throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);

        try {
            connection.setAutoCommit(false);
            triagemDAO.add(triagem);
            connection.commit();

            System.out.println("Triagem realizada com sucesso!");
            return triagem;
        } catch (SQLException e) {
            connection.rollback();

            throw new SQLException("Erro ao realizar triagem: " + e.getMessage());
        }
    }

    public Triagem buscarTriagem(int idTriagem) throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);
        return triagemDAO.buscarTriagem(idTriagem);
    }

    public List<Triagem> listarTriagens() throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);
        return triagemDAO.listarTriagens();
    }

    public void atualizarTriagem(Triagem triagem) throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);

        try {
            connection.setAutoCommit(false);
            triagemDAO.atualizar(triagem);
            connection.commit();

            System.out.println("Triagem atualizada com sucesso");
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Erro ao atualizar triagem.");
        }
    }

    public void deletarTriagem(int idTriagem) throws Exception, SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);

        if (triagemDAO.isTriagemEmUso(idTriagem)){
            throw new Exception("Não é possível deletar triagem em uso.");
        }

        try {
            connection.setAutoCommit(false);
            triagemDAO.deletar(idTriagem);
            connection.commit();

            System.out.println("Triagem deletada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao deletar triagem: " + e.getMessage());
        }
    }
} 
