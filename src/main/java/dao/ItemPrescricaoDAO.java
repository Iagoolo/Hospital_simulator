package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ItemPrescricao;

public class ItemPrescricaoDAO {
    
    private final Connection connection;

    public ItemPrescricaoDAO(Connection connection) {
        this.connection = connection;
    }

    public void addItemPrescricao(ItemPrescricao item) throws SQLException {
        String sql = "INSERT INTO prescricao_item (id_prescricao, id_medicamento, dosagem, frequencia, duracao, observacoes) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, item.getIdPrescricao());
            ps.setInt(2, item.getIdMedicamento());
            ps.setString(3, item.getDosagem());
            ps.setString(4, item.getFrequencia());
            ps.setString(5, item.getDuracao());
            ps.setString(6, item.getObservacoes());
            ps.executeUpdate();
        }
    }

    public void updateItemPrescricao(ItemPrescricao item) throws SQLException {
        String sql = "UPDATE prescricao_item SET dosagem = ?, frequencia = ?, duracao = ?, observacoes = ? WHERE id_prescricao = ? AND id_medicamento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, item.getDosagem());
            ps.setString(2, item.getFrequencia());
            ps.setString(3, item.getDuracao());
            ps.setString(4, item.getObservacoes());
            ps.setInt(5, item.getIdPrescricao());
            ps.setInt(6, item.getIdMedicamento());
            ps.executeUpdate();
        }
    }

    public void deleteItemPrescricao(int idPrescricao, int idMedicamento) throws SQLException {
        String sql = "DELETE FROM prescricao_item WHERE id_prescricao = ? AND id_medicamento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPrescricao);
            ps.setInt(2, idMedicamento);
            ps.executeUpdate();
        }
    }

    public ItemPrescricao getItemPrescricao(int idPrescricao, int idMedicamento) throws SQLException {
        String sql = "SELECT * FROM prescricao_item WHERE id_prescricao = ? AND id_medicamento = ?";
        ItemPrescricao item = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPrescricao);
            ps.setInt(2, idMedicamento);
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

    public List<ItemPrescricao> getAllItemsPrescricao(int idPrescricao) throws SQLException {
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