package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.TriagemDAO;
import com.hospital.model.Triagem;

public class TriagemService {
    private Connection connection;

    public TriagemService(Connection connection){
        this.connection = connection;
    }

    /**
     * Realiza uma triagem e a cadastra no banco de dados.
     * 
     * Este método recebe um objeto {@link Triagem} contendo os detalhes da triagem a ser realizada.
     * A operação de inserção é realizada dentro de uma transação SQL para garantir a integridade
     * dos dados. Se a operação for bem-sucedida, a transação é confirmada; caso contrário, é
     * realizada uma reversão (rollback) para manter o estado consistente do banco de dados.
     * @param triagem O objeto {@link Triagem} contendo os detalhes da triagem a ser realizada.
     * @return O objeto {@link Triagem} que foi cadastrado no banco de dados.
     * @throws SQLException Se ocorrer um erro ao realizar a triagem no banco de dados.
     */
    public Triagem realizarTriagem(Triagem triagem) throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);

        try {
            connection.setAutoCommit(false);
            triagemDAO.add(triagem);
            connection.commit();

            return triagem;
        } catch (SQLException e) {
            connection.rollback();

            throw new SQLException("Erro ao realizar triagem: " + e.getMessage());
        }
    }

    /**
     * Busca uma triagem no banco de dados pelo seu ID.
     * 
     * Este método utiliza a classe {@link TriagemDAO} para buscar e retornar
     * um objeto {@link Triagem} com base no ID fornecido.
     * @param idTriagem O ID da triagem a ser buscada.
     * @return O objeto {@link Triagem} correspondente ao ID fornecido.
     * @throws SQLException Se ocorrer um erro ao buscar a triagem no banco de dados.
     */
    public Triagem buscarTriagem(int idTriagem) throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);
        return triagemDAO.buscarTriagem(idTriagem);
    }

    /**
     * Lista todas as triagens cadastradas no banco de dados.
     * 
     * Este método utiliza a classe {@link TriagemDAO} para buscar e retornar
     * uma lista de todos os objetos {@link Triagem} cadastrados.
     * @return Uma lista de objetos {@link Triagem}.
     * @throws SQLException Se ocorrer um erro ao listar as triagens no banco de dados.
     */
    public List<Triagem> listarTriagens() throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);
        return triagemDAO.listarTriagens();
    }

    /**
     * Atualiza os dados de uma triagem no banco de dados.
     * 
     * Este método recebe um objeto {@link Triagem} com os dados atualizados e
     * realiza a atualização no banco de dados utilizando a classe {@link TriagemDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * @param triagem O objeto {@link Triagem} contendo os dados atualizados.
     * @throws SQLException Se ocorrer um erro ao atualizar a triagem no banco de dados.
     */
    public void atualizarTriagem(Triagem triagem) throws SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);

        try {
            connection.setAutoCommit(false);
            triagemDAO.atualizar(triagem);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Erro ao atualizar triagem.");
        }
    }

    /**
     * Deleta uma triagem do banco de dados com base no ID fornecido.
     * 
     * Este método realiza a exclusão da triagem utilizando a classe {@link TriagemDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * Antes de deletar, verifica se a triagem está em uso.
     * @param idTriagem O ID da triagem a ser deletada.
     * @throws Exception Se a triagem estiver em uso e não puder ser deletada.
     * @throws SQLException Se ocorrer um erro ao deletar a triagem do banco de dados.
     */
    public void deletarTriagem(int idTriagem) throws Exception, SQLException{
        TriagemDAO triagemDAO = new TriagemDAO(connection);

        if (triagemDAO.isTriagemEmUso(idTriagem)){
            throw new Exception("Não é possível deletar triagem em uso.");
        }

        try {
            connection.setAutoCommit(false);
            triagemDAO.deletar(idTriagem);
            connection.commit();

        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao deletar triagem: " + e.getMessage());
        }
    }
} 
