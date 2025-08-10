package dao;

import java.sql.*;
import java.util.List;

public class PacienteDAO {

    private Connection connection;

    public PacienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(model.Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (nome, nomePai, nomeMae, endereco, cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getNomePai());
            stmt.setString(3, paciente.getNomeMae());
            stmt.setString(4, paciente.getEndereco());
            stmt.setString(5, paciente.getCpf());
            stmt.executeUpdate();
        }
    }

    public List<model.Paciente> listarPacientes() throws SQLException {
        String sql = "SELECT * FROM paciente";

        List<model.Paciente> pacientes = new java.util.ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.Paciente paciente = new model.Paciente();

                paciente.setNome(rs.getString("nome"));
                paciente.setNomePai(rs.getString("nomePai"));
                paciente.setNomeMae(rs.getString("nomeMae"));
                paciente.setEndereco(rs.getString("endereco"));
                paciente.setCpf(rs.getString("cpf"));
                pacientes.add(paciente);
            }
        }

        return pacientes;
    }

    public model.Paciente buscarPaciente(String cpf) throws SQLException {
        String sql = "Select * FROM paciente WHERE cpf_paciente = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                model.Paciente paciente = new model.Paciente();

                paciente.setNome(rs.getString("nome"));
                paciente.setNomePai(rs.getString("nomePai"));
                paciente.setNomeMae(rs.getString("nomeMae"));
                paciente.setEndereco(rs.getString("endereco"));
                paciente.setCpf(rs.getString("cpf"));
    
                return paciente;
            }
        }

        return null;
    }

    public void atualizarPaciente(String cpf, String nome, String endereco, String nomePai, String nomeMae) throws SQLException{
        String sql = "UPDATE paciente SET nome = ?, endereco = ?, nomePai = ?, nomeMae = ? WHERE cpf = ? ";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, nome);
            ps.setString(2, endereco);
            ps.setString(3, nomePai);
            ps.setString(4, nomeMae);
            ps.setString(5, cpf);
            ps.executeUpdate();
        }
    }

    public void deletarPaciente(String cpf) throws SQLException {
        String sql = "DELETE FROM paciente WHERE cpf = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);
            ps.executeUpdate();
        }
    }
}
