package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.ItemPrescricao;
import model.Prescricao;

public class PrescricaoDAO {
    
    private Connection connection;

    public PrescricaoDAO(Connection connection) {
        this.connection = connection;
    }

    public void addPrescricao(Prescricao prescricao) throws SQLException {
        String sqlPrescricao = "INSERT INTO prescricao (id_consulta) VALUES (?)";
        String sqlItem = "INSERT INTO item_prescricao (id_prescricao, nome_medicamento, dosagem, frequencia, duracao, instrucoes) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(sqlPrescricao, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, prescricao.getIdConsulta());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idPrescricao = rs.getInt(1);
                        prescricao.setIdPrescricao(idPrescricao);

                        if (prescricao.getItens() != null && !prescricao.getItens().isEmpty()) {
                            try (PreparedStatement psItem = connection.prepareStatement(sqlItem)) {
                                for (ItemPrescricao item : prescricao.getItens()) {
                                    psItem.setInt(1, idPrescricao);
                                    psItem.setString(2, item.getNomeMedicamento());
                                    psItem.setString(3, item.getDosagem());
                                    psItem.setString(4, item.getFrequencia());
                                    psItem.setString(5, item.getDuracao());
                                    psItem.setString(6, item.getInstrucoes());
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

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<ItemPrescricao> getItensByPrescricaoId(int idPrescricao) throws SQLException {
        String sql = "SELECT * FROM item_prescricao WHERE id_prescricao = ?";

        List<ItemPrescricao> itens = new java.util.ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPrescricao);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemPrescricao item = new ItemPrescricao(
                    rs.getInt("id_item"),
                    rs.getInt("id_prescricao"),
                    rs.getString("nome_medicamento"),
                    rs.getString("dosagem"),
                    rs.getString("frequencia"),
                    rs.getString("duracao"),
                    rs.getString("instrucoes")
                );
                itens.add(item);
            }
        }

        return itens;
    }

    public Prescricao getPrescricaosByConsulta(int id_consulta) throws SQLException{
        String sql = "SELECT * FROM prescricao WHERE id_consulta = ?";

        Prescricao prescricao = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id_consulta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prescricao = new Prescricao(
                    rs.getInt("id_prescricao"),
                    rs.getInt("id_consulta")
                );

                prescricao.setItens(getItensByPrescricaoId(prescricao.getIdPrescricao()));
            }
        }

        return prescricao;
    }

    public void deletePrescricao(int idPrescricao) throws SQLException {
        String sqlItem = "DELETE FROM item_prescricao WHERE id_prescricao = ?";
        String sqlPrescricao = "DELETE FROM prescricao WHERE id_prescricao = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement psItem = connection.prepareStatement(sqlItem)) {
                psItem.setInt(1, idPrescricao);
                psItem.executeUpdate();
            }

            try (PreparedStatement psPrescricao = connection.prepareStatement(sqlPrescricao)) {
                psPrescricao.setInt(1, idPrescricao);
                psPrescricao.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}