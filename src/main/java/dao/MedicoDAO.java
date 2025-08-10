package dao;

import java.sql.*;
import java.util.List;

public class MedicoDAO extends PessoaDAO<model.Medico>{

    private Connection connection;

    public MedicoDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void add(model.Medico medico) throws SQLException {
        String sql = "INSERT INTO medico (cpf_medico, turno) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medico.getCpf());
            stmt.setString(2, medico.getTurno());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding medico: " + e.getMessage(), e);
        }
    }

    @Override
    public model.Medico buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM medico WHERE cpf_medico = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                model.Medico medico = new model.Medico();

                medico.setNome(rs.getString("nome"));
                medico.setCpf(rs.getString("cpf_medico"));
                medico.setTurno(rs.getString("turno"));

                return medico;
            }
        } catch (SQLException e) {
            throw new SQLException("Error searching medico: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<model.Medico> listarTodos() throws SQLException {
        String sql = "SELECT * FROM medico";
        List<model.Medico> medicos = new java.util.ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                model.Medico medico = new model.Medico();

                medico.setNome(rs.getString("nome"));
                medico.setCpf(rs.getString("cpf_medico"));
                medico.setTurno(rs.getString("turno"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            throw new SQLException("Error searching medico: " + e.getMessage(), e);
        }

        return medicos;
    }

    @Override
     public void atualizar(model.Medico medico) throws SQLException{
        String sql = "UPDATE medico SET nome = ?, turno = ? WHERE cpf_medico = ? ";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getTurno());
            ps.setString(3, medico.getCpf());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating medico: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(String cpfMedico) throws SQLException {
        String sql = "DELETE FROM medico WHERE cpf_medico = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfMedico);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting medico: " + e.getMessage(), e);
        }
    }
}
