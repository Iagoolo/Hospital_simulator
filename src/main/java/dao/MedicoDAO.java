package dao;

import java.sql.*;

public class MedicoDAO {

    private Connection connection;

    public MedicoDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(model.Medico medico) throws SQLException {
        String sql = "INSERT INTO medicos (cpf_medico, turno) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medico.getCpfMedico());
            stmt.setString(2, medico.getTurno());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding medico: " + e.getMessage(), e);
        }
    }
}
