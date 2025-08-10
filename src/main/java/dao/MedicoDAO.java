package dao;

import java.sql.*;
import java.util.List;

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

    public model.Medico buscarMedico(String cpf) throws SQLException {
        String sql = "SELECT * FROM medicos WHERE cpf_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                model.Medico medico = new model.Medico();

                medico.setNome(rs.getString("nome"));
                medico.setCpfMedico(rs.getString("cpf_medico"));
                medico.setTurno(rs.getString("turno"));

                return medico;
            }
        } catch (SQLException e) {
            throw new SQLException("Error searching medico: " + e.getMessage(), e);
        }

        return null;
    }

    public List<model.Medico> listarMedicos() throws SQLException {
        String sql = "SELECT * FROM medicos";
        List<model.Medico> medicos = new java.util.ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                model.Medico medico = new model.Medico();

                medico.setNome(rs.getString("nome"));
                medico.setCpfMedico(rs.getString("cpf_medico"));
                medico.setTurno(rs.getString("turno"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            throw new SQLException("Error searching medico: " + e.getMessage(), e);
        }

        return medicos;
    }

     public void atualizarPaciente(String cpf, String nome, String turno) throws SQLException{
        String sql = "UPDATE medico SET nome = ?, turno = ? WHERE cpf = ? ";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, nome);
            ps.setString(2, turno);
            ps.setString(3, cpf);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating medico: " + e.getMessage(), e);
        }
    }

    public void deletarPaciente(String cpf) throws SQLException {
        String sql = "DELETE FROM medico WHERE cpf = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting medico: " + e.getMessage(), e);
        }
    }
}
