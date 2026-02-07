package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.EnfermeiroDAO;
import com.hospital.model.Enfermeiro;

public class EnfermeiroService {
    
    private Connection connection;

    public EnfermeiroService (Connection connection){
        this.connection = connection;
    }

    /**
     * Cadastra um novo enfermeiro no banco de dados com controle de transação.
     * 
     * Este método utiliza transações para garantir a integridade dos dados.
     * Se o cadastro for bem-sucedido, a transação é confirmada (commit).
     * Em caso de erro, a transação é revertida (rollback) e uma exceção é lançada.
     * 
     * @param enfermeiro o objeto Enfermeiro contendo os dados a serem cadastrados
     * 
     * @throws SQLException se ocorrer um erro durante o cadastro do enfermeiro
     *                      ou se houver problema ao reverter a transação
     * 
     * @see EnfermeiroDAO#add(Enfermeiro)
     */
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

    /**
     * Atualiza os dados de um enfermeiro no banco de dados.
     * 
     * Este método utiliza uma transação para garantir a integridade dos dados.
     * Se a atualização for bem-sucedida, a transação é confirmada (commit).
     * Em caso de erro, a transação é desfeita (rollback).
     * 
     * @param enfermeiro o objeto {@link Enfermeiro} contendo os dados atualizados
     *                   a serem persistidos no banco de dados
     * 
     * @throws SQLException se ocorrer um erro durante a atualização do enfermeiro
     *                      no banco de dados ou se houver problemas com a transação
     * 
     * @see EnfermeiroDAO#atualizar(Enfermeiro)
     */
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

    /**
     * Deleta um enfermeiro do banco de dados pelo CPF.
     * 
     * <p>Este método executa a operação de exclusão de um enfermeiro dentro de uma transação,
     * garantindo que a operação seja atômica. Se a exclusão for bem-sucedida, a transação é
     * confirmada. Em caso de erro, a transação é desfeita (rollback) e uma exceção é lançada.</p>
     * 
     * @param cpfEnfermeiro o CPF do enfermeiro a ser deletado
     * @throws SQLException se ocorrer um erro durante a operação de exclusão no banco de dados
     */
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

    /**
     * Busca um enfermeiro no banco de dados através do seu número de CPF.
     *
     * @param cpfEnfermeiro o CPF do enfermeiro a ser procurado
     * @return um objeto {@link Enfermeiro} correspondente ao CPF informado
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     */
    public Enfermeiro buscarEnfermeiroCpf(String cpfEnfermeiro) throws SQLException{
        EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(connection);
        return enfermeiroDAO.buscarPorCpf(cpfEnfermeiro);
    }

    /**
     * Lista todos os enfermeiros registrados no sistema.
     * 
     * @return uma lista contendo todos os enfermeiros cadastrados
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     */
    public List<Enfermeiro> listarTodosEnfermeiros() throws SQLException{
        EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(connection);
        return enfermeiroDAO.listarTodos();
    }
}
