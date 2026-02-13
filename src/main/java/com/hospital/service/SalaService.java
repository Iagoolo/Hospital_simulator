package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.SalaDAO;
import com.hospital.model.Sala;

// Serviço para gerenciar operações relacionadas às salas do hospital
public class SalaService {
    private Connection connection;

    public SalaService (Connection connection){
        this.connection = connection;
    }

    /**
     * Cadastra uma nova sala no sistema
     * @param sala
     * @return A sala cadastrada
     * @throws SQLException em caso de erro ao acessar o banco de dados
     */
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

    /**
     * Deleta uma sala do sistema
     * @param idSala
     * @throws SQLException em caso de erro ao acessar o banco de dados
     * @throws Exception em caso de tentativa de deletar uma sala em uso
     */
    public void deletarSala (int idSala) throws SQLException, Exception {
        SalaDAO salaDAO = new SalaDAO(connection);

        if (salaDAO.isSalaEmUso(idSala)){
            throw new Exception("Não é possível deletar uma sala em uso");
        }

        try {
            connection.setAutoCommit(false);
            salaDAO.deletar(idSala);
            connection.commit();

        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao deletar sala: " + e.getMessage());
        }
    }

    /**
     * Atualiza as informações de uma sala
     * @param sala
     * @throws SQLException em caso de erro ao acessar o banco de dados
     */
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

    /**
     * Lista todas as salas cadastradas no sistema
     * @return Lista de salas
     * @throws SQLException em caso de erro ao acessar o banco de dados
     */
    public List<Sala> listarTodasSalas() throws SQLException{
        SalaDAO salaDAO = new SalaDAO(connection);
        return salaDAO.listar();
    }

    /**
     * Busca uma sala pelo seu ID
     * @param idSala
     * @return A sala encontrada
     * @throws SQLException em caso de erro ao acessar o banco de dados
     */
    public Sala buscarSala(int idSala) throws SQLException{
        SalaDAO salaDAO = new SalaDAO(connection);
        return salaDAO.buscarSala(idSala);
    }

    /**
     * Lista todas as salas livres para uso
     * @return lista de salas
     * @throws SQLException
     */
    public List<Sala> salasLivres() throws SQLException{
        SalaDAO salaDAO = new SalaDAO(connection);
        return salaDAO.todasLivreSalas();
    }
}