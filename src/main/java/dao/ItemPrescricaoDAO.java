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

    public void delete(int idItem) throws SQLException {
        String sql = "DELETE FROM prescricao_item WHERE id_item = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            ps.executeUpdate();
        }
    }

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