package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.ExamesDAO;
import com.hospital.model.Exames;

public class ExamesService {
    private Connection connection;
    
    public ExamesService(Connection connection){
        this.connection = connection;
    }

    /**
     * Cadastra um novo exame no sistema.
     *
     * Este método recebe um objeto do tipo Exames, adiciona-o ao banco de dados
     * utilizando a classe ExamesDAO e gerencia a transação com commit e rollback
     * em caso de erro.
     *
     * @param exame O objeto Exames a ser cadastrado.
     * @return O objeto Exames cadastrado.
     * @throws SQLException Se ocorrer um erro ao cadastrar o exame no banco de dados.
     */
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

    /**
     * Deleta um exame do banco de dados com base no ID fornecido.
     *
     * @param idExame O ID do exame a ser deletado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados ou ao deletar o exame.
     */
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

    /**
     * Atualiza as informações de um exame no banco de dados.
     *
     * Este método utiliza a classe ExamesDAO para realizar a atualização do exame
     * fornecido. A operação é realizada dentro de uma transação, garantindo que
     * as alterações sejam aplicadas apenas se não ocorrerem erros durante o processo.
     *
     * @param exame O objeto Exames que contém as informações a serem atualizadas.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados ou ao
     *                      realizar a atualização.
     */
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

    /**
     * Busca um exame pelo seu identificador único.
     *
     * @param idExame o identificador do exame a ser buscado
     * @return o objeto Exames correspondente ao idExame
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     */
    public Exames buscarExame(int idExame) throws SQLException{
        ExamesDAO examesDAO = new ExamesDAO(connection);
        return examesDAO.buscar(idExame);
    }

    /**
     * Lista todos os exames associados a uma consulta específica.
     *
     * @param idConsulta O ID da consulta para a qual os exames devem ser listados.
     * @return Uma lista de objetos Exames relacionados à consulta especificada.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    public List<Exames> listarTodosExames(int idConsulta) throws SQLException{
        ExamesDAO examesDAO = new ExamesDAO(connection);
        return examesDAO.listar(idConsulta);
    }
}
