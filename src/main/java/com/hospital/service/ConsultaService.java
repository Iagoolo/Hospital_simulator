package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.ConsultaDAO;
import com.hospital.model.Consulta;

public class ConsultaService {
    private Connection connection;

    public ConsultaService(Connection connection){
        this.connection = connection;
    }
    
    /**
     * Cria uma nova consulta no banco de dados.
     * 
     * Este método insere uma nova consulta na base de dados utilizando transações.
     * Se a operação for bem-sucedida, a transação é confirmada (commit).
     * Em caso de erro, a transação é desfeita (rollback) e uma exceção é lançada.
     * 
     * @param consulta A consulta a ser criada. Não deve ser nula.
     * @return A consulta criada com sucesso.
     * @throws SQLException Se ocorrer um erro durante a criação da consulta no banco de dados.
     *                      A mensagem de erro inclui detalhes da exceção original.
     * 
     * @see ConsultaDAO
     */
    public Consulta criarConsulta(Consulta consulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);
        
        try{
            connection.setAutoCommit(false);
            consultaDAO.add(consulta);
            connection.commit();

            System.out.println("Consulta criada com sucesso!");
            return consulta;
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao criar consulta: " + e.getMessage());
        }
    }

    /**
     * Deleta uma consulta do banco de dados.
     * 
     * <p>Este método remove uma consulta específica identificada pelo seu ID.
     * A operação é executada dentro de uma transação que é confirmada apenas
     * se todas as operações forem bem-sucedidas. Em caso de erro, a transação
     * é desfeita (rollback).</p>
     * 
     * @param idConsulta o identificador único da consulta a ser deletada
     * 
     * @throws SQLException se ocorrer um erro durante a operação de exclusão
     *                      no banco de dados ou se a transação falhar
     * 
     * @see ConsultaDAO#deletar(int)
     */
    public void deletarConsulta(int idConsulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);
        
        try {
            connection.setAutoCommit(false);
            consultaDAO.deletar(idConsulta);
            connection.commit();

            System.out.println("Consulta deletada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao deletar consulta: " + e.getMessage());
        }
    }

    /**
     * Procura uma consulta no banco de dados pelo seu identificador.
     * 
     * @param idConsulta o identificador da consulta a ser procurada
     * @return um objeto {@link Consulta} correspondente ao identificador fornecido
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     */
    public Consulta procurarConsultaId(int idConsulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);
        return consultaDAO.buscarConsulta(idConsulta);
    }

    /**
     * Lista todas as consultas que estão pendentes, ou seja, aquelas que ainda não possuem diagnóstico.
     * 
     * Este método consulta a base de dados através do objeto {@link ConsultaDAO} para recuperar
     * todas as consultas sem diagnóstico registrado.
     * 
     * @return uma {@link List} contendo os objetos {@link Consulta} pendentes de diagnóstico
     * @throws SQLException se ocorrer um erro durante a consulta ao banco de dados
     */
    public List<Consulta> listarConsultasPendentes() throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);
        return consultaDAO.listarConsultasSemDiagnostico();
    }

    /**
     * Atualiza os dados de uma consulta existente no banco de dados.
     * 
     * Este método executa a atualização de uma consulta dentro de uma transação.
     * Caso a operação seja bem-sucedida, a transação é confirmada (commit).
     * Em caso de erro, a transação é desfeita (rollback) e uma exceção é lançada.
     * 
     * @param consulta objeto {@link Consulta} contendo os dados atualizados da consulta
     * 
     * @throws SQLException se ocorrer um erro durante a atualização da consulta no banco de dados
     * 
     * @see ConsultaDAO#atualizarConsulta(Consulta)
     */
    public void atualizarConsulta(Consulta consulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);

        try {
            connection.setAutoCommit(false);
            consultaDAO.atualizarConsulta(consulta);
            connection.commit();

            System.out.println("Consulta atualizada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao atualizar consulta: " + e.getMessage());
        }
    }
}
