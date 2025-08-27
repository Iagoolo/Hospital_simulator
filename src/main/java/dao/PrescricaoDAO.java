package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ItemPrescricao;
import model.Prescricao;

public class PrescricaoDAO {
    
    private final Connection connection;

    public PrescricaoDAO(Connection connection) {
        this.connection = connection;
    }

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