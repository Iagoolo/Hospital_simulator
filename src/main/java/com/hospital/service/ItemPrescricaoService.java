package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.ItemPrescricaoDAO;
import com.hospital.model.ItemPrescricao;

public class ItemPrescricaoService {
    private Connection connection;

    public ItemPrescricaoService(Connection connection){
        this.connection = connection;
    }

    /**
     * Adiciona um item de prescrição ao banco de dados.
     * 
     * Este método recebe um objeto {@link ItemPrescricao} e o adiciona ao banco de dados
     * utilizando a classe {@link ItemPrescricaoDAO}. A operação é realizada dentro de uma
     * transação SQL para garantir atomicidade.
     * @param item
     * @return
     * @throws SQLException
     */
    public ItemPrescricao addItem(ItemPrescricao item) throws SQLException{
        ItemPrescricaoDAO itemPrescricaoDAO = new ItemPrescricaoDAO(connection);

        try{
            connection.setAutoCommit(false);
            itemPrescricaoDAO.add(item);
            connection.commit();

            System.out.println("Item adicionado com sucesso!");
            return item;
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao adicionar item: " + e.getMessage());
        }
    }

    /** 
     * Atualiza um item de prescrição no banco de dados.
     * 
     * Este método recebe um objeto {@link ItemPrescricao} e atualiza seus dados no banco de dados
     * utilizando a classe {@link ItemPrescricaoDAO}. A operação é realizada dentro de uma
     * transação SQL para garantir atomicidade.
     * @param item
     * @throws SQLException
     */
    public void atualizarItem(ItemPrescricao item) throws SQLException{
        ItemPrescricaoDAO itemPrescricaoDAO = new ItemPrescricaoDAO(connection);

        try {
            connection.setAutoCommit(false);
            itemPrescricaoDAO.update(item);
            connection.commit();

            System.out.println("Item atualizado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao atualizar item: " + e.getMessage());
        }
    }

    /**
     * Deleta um item de prescrição do banco de dados com base no ID do item.
     * 
     * Este método executa a operação de exclusão dentro de uma transação, garantindo
     * que a operação seja atômica. Se a exclusão for bem-sucedida, a transação é
     * confirmada. Em caso de erro, a transação é desfeita (rollback) e uma exceção
     * é lançada com detalhes do erro.
     * @param idItem
     * @throws SQLException
     */
    public void deletarItem(int idItem) throws SQLException{
        ItemPrescricaoDAO itemPrescricaoDAO = new ItemPrescricaoDAO(connection);

        try {
            connection.setAutoCommit(false);
            itemPrescricaoDAO.delete(idItem);
            connection.commit();

            System.out.println("Item deletado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar item: " + e.getMessage());
        }
    }

    /**
     * Busca um item de prescrição no banco de dados pelo seu ID.
     * 
     * Este método utiliza a classe {@link ItemPrescricaoDAO} para buscar e retornar
     * um objeto {@link ItemPrescricao} com base no ID fornecido.
     * @param idItem
     * @return
     * @throws SQLException
     */
    public ItemPrescricao buscarItem(int idItem) throws SQLException{
        ItemPrescricaoDAO itemPrescricaoDAO = new ItemPrescricaoDAO(connection);
        return itemPrescricaoDAO.buscar(idItem);
    }

    /**
     * Lista todos os itens de prescrição associados a uma prescrição específica.
     * 
     * Este método utiliza a classe {@link ItemPrescricaoDAO} para buscar e retornar
     * uma lista de objetos {@link ItemPrescricao} associados ao ID da prescrição fornecida.
     * @param idPrescricao
     * @return
     * @throws SQLException
     */
    public List<ItemPrescricao> listar(int idPrescricao) throws SQLException{
        ItemPrescricaoDAO itemPrescricaoDAO = new ItemPrescricaoDAO(connection);
        return itemPrescricaoDAO.listar(idPrescricao);
    }
}
