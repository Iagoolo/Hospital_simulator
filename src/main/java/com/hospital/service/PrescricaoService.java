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

    /**
     * Cria uma nova prescrição no banco de dados.
     * 
     * Este método recebe um objeto {@link Prescricao} contendo os detalhes da prescrição a ser criada.
     * A operação de inserção é realizada dentro de uma transação SQL para garantir a integridade
     * dos dados. Se a operação for bem-sucedida, a transação é confirmada; caso contrário, é
     * realizada uma reversão (rollback) para manter o estado consistente do banco de dados.
     * @param prescricao O objeto {@link Prescricao} contendo os detalhes da prescrição a ser criada.
     * @throws SQLException Se ocorrer um erro ao criar a prescrição no banco de dados.
     */
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

    /**
     * Deleta uma prescrição do banco de dados com base no ID fornecido.
     * 
     * Este método realiza a exclusão da prescrição utilizando a classe {@link PrescricaoDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * @param idPrescricao O ID da prescrição a ser deletada.
     * @throws SQLException Se ocorrer um erro ao deletar a prescrição do banco de dados.
     */
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

    /**
     * Busca uma prescrição no banco de dados com base no ID da consulta.
     * 
     * Este método utiliza a classe {@link PrescricaoDAO} para buscar e retornar
     * um objeto {@link Prescricao} associado ao ID da consulta fornecido.
     * @param idConsulta O ID da consulta para a qual a prescrição será buscada.
     * @return O objeto {@link Prescricao} correspondente à consulta.
     * @throws SQLException Se ocorrer um erro ao buscar a prescrição no banco de dados.
     */
    public Prescricao buscaPrescricaoConsulta(int idConsulta) throws SQLException{
        PrescricaoDAO prescricaoDAO = new PrescricaoDAO(connection);
        return prescricaoDAO.getPrescricaosByConsulta(idConsulta);
    }
}
