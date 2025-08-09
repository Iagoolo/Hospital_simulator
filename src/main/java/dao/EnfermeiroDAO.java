package dao;

import java.sql.*;

public class EnfermeiroDAO {

    private Connection connection;

    public EnfermeiroDAO(Connection connection){
        this.connection = connection;
    }

    public void add(model.Enfermeiro enfermeiro) throws SQLException{
        String sql = "INSERT INTO enfermeiro (cpf_enfermeiro) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, enfermeiro.getCpfEnfermeiro());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding enfermeiro: " + e.getMessage(), e);
        }
    }
}
