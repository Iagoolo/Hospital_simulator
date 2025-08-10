package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO extends PessoaDAO<model.Paciente> {

    private Connection connection;

    public PacienteDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void add(model.Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (nome, nomePai, nomeMae, endereco, cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getNomePai());
            stmt.setString(3, paciente.getNomeMae());
            stmt.setString(4, paciente.getEndereco());
            stmt.setString(5, paciente.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<model.Paciente> listarTodos() throws SQLException {
        List<model.Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";

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
        } catch (SQLException e) {
            throw new SQLException("Error listing pacientes: " + e.getMessage(), e);
        }

        return pacientes;
    }

    @Override
    public model.Paciente buscarPorCpf(String cpf) throws SQLException {
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
        } catch (SQLException e) {
            throw new SQLException("Error searching paciente: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public void atualizar(model.Paciente paciente) throws SQLException{
        String sql = "UPDATE paciente SET nome = ?, endereco = ?, nomePai = ?, nomeMae = ? WHERE cpf = ? ";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getEndereco());
            ps.setString(3, paciente.getNomePai());
            ps.setString(4, paciente.getNomeMae());
            ps.setString(5, paciente.getCpf());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(String cpf) throws SQLException {
        String sql = "DELETE FROM paciente WHERE cpf = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting paciente: " + e.getMessage(), e);
        }
    }
}
