package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.ItemPrescricaoDAO;
import model.ItemPrescricao;

public class ItemPrescricaoService {
    private Connection connection;

    public ItemPrescricaoService(Connection connection){
        this.connection = connection;
    }

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

    public ItemPrescricao buscarItem(int idItem) throws SQLException{
        ItemPrescricaoDAO itemPrescricaoDAO = new ItemPrescricaoDAO(connection);
        return itemPrescricaoDAO.buscar(idItem);
    }

    public List<ItemPrescricao> listar(int idPrescricao) throws SQLException{
        ItemPrescricaoDAO itemPrescricaoDAO = new ItemPrescricaoDAO(connection);
        return itemPrescricaoDAO.listar(idPrescricao);
    }
}
