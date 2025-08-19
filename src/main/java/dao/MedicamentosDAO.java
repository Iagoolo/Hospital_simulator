package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Medicamento;

public class MedicamentosDAO {
    private Connection connection;

    public MedicamentosDAO(Connection connection) {
        this.connection = connection;
    }

    public void addMedicamento(Medicamento medicamento) throws SQLException{
        String sql = "INSERT INTO medicamentos (nome, formula, forma, via_administracao) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, medicamento.getNome());
            ps.setString(2, medicamento.getFormula());
            ps.setString(3, medicamento.getForma());
            ps.setString(4, medicamento.getViaAdministracao());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                medicamento.setIdMedicamento(rs.getInt(1));
            }
        }
    }

    public void deletarMedicamento(int idMedicamento) throws SQLException{
        String sql = "DELETE FROM medicamentos WHERE id_medicamento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idMedicamento);
            ps.executeUpdate();
        }
    }

    public void updateMedicamento(Medicamento medicamento) throws SQLException{
        String sql = "UPDATE medicamentos SET nome = ?, formula = ?, forma = ?, via_administracao = ? WHERE id_medicamento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, medicamento.getNome());
            ps.setString(2, medicamento.getFormula());
            ps.setString(3, medicamento.getForma());
            ps.setString(4, medicamento.getViaAdministracao());
            ps.setInt(5, medicamento.getIdMedicamento());
            ps.executeUpdate();
        }   
    }

    public Medicamento buscarMedicamento (int idMedicamento) throws SQLException {
        String sql = "SELECT * FROM medicamentos WHERE id_medicamento = ?";
        Medicamento medicamento = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            var rs = ps.executeQuery();
            if (rs.next()) {
                medicamento = new Medicamento(
                    rs.getInt("id_medicamento"),
                    rs.getString("nome"),
                    rs.getString("formula"),
                    rs.getString("forma"),
                    rs.getString("via_administracao")
                );
            }
        }
        return medicamento;
    }

    public List<Medicamento> buscarAllMedicamentos() throws SQLException {
        String sql = "SELECT * FROM medicamentos";
        List<Medicamento> medicamentos = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            var rs = ps.executeQuery();
            while (rs.next()) {
                Medicamento medicamento = new Medicamento(
                    rs.getInt("id_medicamento"),
                    rs.getString("nome"),
                    rs.getString("formula"),
                    rs.getString("forma"),
                    rs.getString("via_administracao")
                );
                medicamentos.add(medicamento);
            }
        }
        return medicamentos;
    }
}