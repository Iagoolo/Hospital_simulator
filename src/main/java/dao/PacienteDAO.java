package dao;

import java.sql.*;

public class PacienteDAO {

    private Connection connection;

    public PacienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(model.Paciente paciente) throws SQLException {
        String sql = "INSERT INTO pacientes (nome, nomePai, nomeMae, endereco, cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getNomePai());
            stmt.setString(3, paciente.getNomeMae());
            stmt.setString(4, paciente.getEndereco());
            stmt.setString(5, paciente.getCpf());
            stmt.executeUpdate();
        }
    }
}
