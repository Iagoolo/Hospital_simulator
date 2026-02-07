package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hospital.model.ItemPrescricao;

public class ItemPrescricaoDAO {
    
    private final Connection connection;

    public ItemPrescricaoDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adiciona um novo item de prescrição ao banco de dados.
     * 
     * <p>Este método insere um novo registro na tabela de itens de prescrição, associando-o
     * a uma prescrição e medicamento específicos. O ID do item é gerado automaticamente
     * pelo banco de dados e atualizado no objeto ItemPrescricao após a inserção.</p>
     * 
     * @param item o objeto ItemPrescricao contendo os detalhes do item a ser adicionado. O campo idItem será atualizado 
     * com o ID gerado pelo banco de dados.
     * @throws SQLException se ocorrer um erro durante a execução da inserção no banco de dados ou ao processar os resultados.
     */
    public void add(ItemPrescricao item) throws SQLException {
        String sql = "INSERT INTO prescricao_item (id_prescricao, id_medicamento, dosagem, frequencia, duracao, observacoes) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getIdPrescricao());
            ps.setInt(2, item.getIdMedicamento());
            ps.setString(3, item.getDosagem());
            ps.setString(4, item.getFrequencia());
            ps.setString(5, item.getDuracao());
            ps.setString(6, item.getObservacoes());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                item.setIdItem(rs.getInt(1));
            }
        }
    }

    /**
     * Atualiza os detalhes de um item de prescrição existente no banco de dados.
     * 
     * <p>Este método executa uma instrução SQL para atualizar os campos dosagem, frequencia, duracao, observacoes, 
     * id_prescricao e id_medicamento de um item de prescrição identificado pelo seu ID. Se o item não existir, 
     * a operação não terá efeito.</p>
     * 
     * @param item o objeto ItemPrescricao contendo os detalhes atualizados do item. O campo idItem deve estar 
     * definido para identificar o item a ser atualizado.
     * @throws SQLException se ocorrer um erro durante a execução da atualização no banco de dados.
     */
    public void update(ItemPrescricao item) throws SQLException {
        String sql = "UPDATE prescricao_item SET dosagem = ?, frequencia = ?, duracao = ?, observacoes = ?, id_prescricao = ?, id_medicamento = ? WHERE id_item = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, item.getDosagem());
            ps.setString(2, item.getFrequencia());
            ps.setString(3, item.getDuracao());
            ps.setString(4, item.getObservacoes());
            ps.setInt(5, item.getIdPrescricao());
            ps.setInt(6, item.getIdMedicamento());
            ps.setInt(7, item.getIdItem());
            ps.executeUpdate();
        }
    }

    /**
     * Deleta um item de prescrição do banco de dados com base no seu ID.
     * 
     * <p>Este método executa uma instrução SQL para remover um item de prescrição identificado
     * pelo seu ID. Se o item não existir, a operação não terá efeito.</p>
     * 
     * @param idItem o ID do item de prescrição a ser deletado.
     * @throws SQLException se ocorrer um erro durante a execução da deleção no banco de dados.
     */
    public void delete(int idItem) throws SQLException {
        String sql = "DELETE FROM prescricao_item WHERE id_item = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            ps.executeUpdate();
        }
    }
    
    /**
     * Busca um item de prescrição no banco de dados com base no seu ID.
     * 
     * <p>Este método executa uma consulta SQL para recuperar um item de prescrição identificado pelo seu ID. 
     * Ele retorna um objeto ItemPrescricao contendo os detalhes do item, ou null se nenhum item for encontrado para o ID fornecido.</p>
     * 
     * @param idItem o ID do item de prescrição a ser buscado.
     * @return um objeto ItemPrescricao contendo os detalhes do item de prescrição, ou null se 
     * nenhum item for encontrado para o ID fornecido.
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados ou ao processar os resultados.
     */
    public ItemPrescricao buscar(int idItem) throws SQLException {
        String sql = "SELECT * FROM prescricao_item WHERE id_item = ?";
        ItemPrescricao item = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                item = new ItemPrescricao(
                    rs.getInt("id_item"),
                    rs.getInt("id_prescricao"),
                    rs.getInt("id_medicamento"),
                    rs.getString("dosagem"),
                    rs.getString("frequencia"),
                    rs.getString("duracao"),
                    rs.getString("observacoes")
                );
            }
        }
        return item;
    }

    /**
     * Lista todos os itens de prescrição associados a uma prescrição específica.
     * 
     * <p>Este método executa uma consulta SQL para recuperar todos os itens de prescrição associados a uma prescrição 
     * identificada pelo seu ID. Ele retorna uma lista de objetos ItemPrescricao contendo os detalhes de cada item encontrado.</p>
     * 
     * @param idPrescricao o ID da prescrição cujos itens devem ser listados.
     * @return uma lista de objetos ItemPrescricao contendo os detalhes dos itens de prescrição associados 
     * à prescrição especificada. A lista pode estar vazia se não houver itens associados à prescrição.
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados ou ao processar os resultados.
     */
    public List<ItemPrescricao> listar(int idPrescricao) throws SQLException {
        String sql = "SELECT * FROM prescricao_item WHERE id_prescricao = ?";
        List<ItemPrescricao> items = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPrescricao);
            var rs = ps.executeQuery();
            while (rs.next()) {
                ItemPrescricao item = new ItemPrescricao(
                    rs.getInt("id_item"),
                    rs.getInt("id_prescricao"),
                    rs.getInt("id_medicamento"),
                    rs.getString("dosagem"),
                    rs.getString("frequencia"),
                    rs.getString("duracao"),
                    rs.getString("observacoes")
                );
                items.add(item);
            }
        }
        return items;
    }
}