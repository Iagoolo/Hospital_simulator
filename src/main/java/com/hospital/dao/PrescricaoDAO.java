package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hospital.model.ItemPrescricao;
import com.hospital.model.Prescricao;

public class PrescricaoDAO {
    
    private final Connection connection;

    public PrescricaoDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adiciona uma nova prescrição ao banco de dados junto com seus itens.
     * 
     * Este método insere uma prescrição e todos os seus itens associados (medicamentos)
     * em uma transação. A prescrição é inserida primeiro, gerando um ID automático que
     * é utilizado para vincular os itens de prescrição.
     * 
     * @param prescricao a prescrição a ser adicionada ao banco de dados. Deve conter
     *                   um ID de consulta válido e pelo menos um item de prescrição.
     *                   O ID da prescrição será preenchido automaticamente após a inserção.
     * 
     * @throws SQLException se ocorrer um erro durante a operação no banco de dados,
     *                      ou se a prescrição não contiver nenhum item. A mensagem
     *                      de erro "A prescrição deve conter pelo menos um item." é
     *                      lançada quando a lista de itens está vazia ou nula.
     * 
     * @see Prescricao
     * @see ItemPrescricao
     */
    public void add(Prescricao prescricao) throws SQLException {
        String sqlPrescricao = "INSERT INTO prescricao (id_consulta) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlPrescricao, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, prescricao.getIdConsulta());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idPrescricao = rs.getInt(1);
                    prescricao.setIdPrescricao(idPrescricao);
                    
                    if (prescricao.getItens() != null && !prescricao.getItens().isEmpty()) {
                        String sqlItem = "INSERT INTO prescricao_item (id_prescricao, id_medicamento, dosagem, frequencia, duracao, observacoes) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement psItem = connection.prepareStatement(sqlItem)) {
                            for (ItemPrescricao item : prescricao.getItens()) {
                                psItem.setInt(1, idPrescricao);
                                psItem.setInt(2, item.getIdMedicamento());
                                psItem.setString(3, item.getDosagem());
                                psItem.setString(4, item.getFrequencia());
                                psItem.setString(5, item.getDuracao());
                                psItem.setString(6, item.getObservacoes());
                                psItem.addBatch();
                            }
                            psItem.executeBatch();
                        }
                    } else {
                        throw new SQLException("A prescrição deve conter pelo menos um item.");
                    }
                }
            }
        }
    }

    /**
     * Recupera uma prescrição com todos os seus itens associados a partir do ID da consulta.
     * 
     * <p>Este método executa uma consulta SQL que utiliza LEFT JOIN para obter a prescrição
     * e todos os itens de medicamentos associados em uma única operação. A prescrição é
     * construída na primeira iteração do resultado, e os itens são adicionados à medida
     * que são encontrados no ResultSet.</p>
     * 
     * @param id_consulta o identificador único da consulta para a qual se deseja obter a prescrição
     * @return um objeto {@code Prescricao} contendo todos os itens associados, ou {@code null}
     *         caso nenhuma prescrição seja encontrada para a consulta informada
     * @throws SQLException se ocorrer um erro durante a execução da consulta ao banco de dados
     * 
     * @see Prescricao
     * @see ItemPrescricao
     */
    public Prescricao getPrescricaosByConsulta(int id_consulta) throws SQLException {
        String sql = """
            SELECT 
                p.id_prescricao, p.id_consulta, pi.*
            FROM 
                prescricao p
            LEFT JOIN 
                prescricao_item pi ON p.id_prescricao = pi.id_prescricao
            WHERE p.id_consulta = ?
        """;

        Prescricao prescricao = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id_consulta);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (prescricao == null) {
                    prescricao = new Prescricao(
                        rs.getInt("id_prescricao"),
                        rs.getInt("id_consulta")
                    );
                }

                int idItem = rs.getInt("id_item");
                if (idItem > 0) {
                    ItemPrescricao item = new ItemPrescricao(
                        idItem,
                        rs.getInt("id_prescricao"),
                        rs.getInt("id_medicamento"),
                        rs.getString("dosagem"),
                        rs.getString("frequencia"),
                        rs.getString("duracao"),
                        rs.getString("observacoes")
                    );
                    prescricao.addItem(item);
                }
            }
        }
        return prescricao;
    }

    /**
     * Deletes a prescription and all its associated items from the database.
     * <p>
     * This method performs a two-step deletion:
     * <ol>
     *   <li>First, deletes all items ({@code item_prescricao}) associated with the prescription</li>
     *   <li>Then, deletes the prescription ({@code prescricao}) itself</li>
     * </ol>
     * </p>
     *
     * @param idPrescricao the ID of the prescription to be deleted
     * @throws SQLException if a database access error occurs during the deletion process
     */
    public void delete(int idPrescricao) throws SQLException {
        
        String sqlItem = "DELETE FROM item_prescricao WHERE id_prescricao = ?";
        try (PreparedStatement psItem = connection.prepareStatement(sqlItem)) {
            psItem.setInt(1, idPrescricao);
            psItem.executeUpdate();
        }
        
        String sqlPrescricao = "DELETE FROM prescricao WHERE id_prescricao = ?";
        try (PreparedStatement psPrescricao = connection.prepareStatement(sqlPrescricao)) {
            psPrescricao.setInt(1, idPrescricao);
            psPrescricao.executeUpdate();
        }
    }
}