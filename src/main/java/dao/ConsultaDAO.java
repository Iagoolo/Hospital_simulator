package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Consulta;

public class ConsultaDAO {
    private Connection connection;

    public ConsultaDAO(Connection connection) {
        this.connection = connection;
    }

    public void addConsulta(Consulta consulta) throws SQLException{
        String sql = "INSERT INTO consulta (id_triagem, sala, data_consulta, hora_consulta, observacao, diagnostico, cpf_paciente, cpf_medico) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, consulta.getIdTriagem());
            ps.setInt(2, consulta.getSala());
            ps.setDate(3, java.sql.Date.valueOf(consulta.getDataConsulta()));
            ps.setTime(4, java.sql.Time.valueOf(consulta.getHoraConsulta()));
            ps.setString(5, consulta.getObservacao());
            ps.setString(6, consulta.getDiagnostico());
            ps.setString(7, consulta.getCpfPaciente());
            ps.setString(8, consulta.getCpfMedico());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                consulta.setIdConsulta(rs.getInt(1));
            }
        }
    }

    public Consulta buscarConsulta(int idConsulta) throws SQLException {
        String sql = "SELECT * FROM consulta WHERE id_consulta = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Consulta(
                    rs.getInt("id_consulta"),
                    rs.getInt("id_triagem"),
                    rs.getInt("sala"),
                    rs.getDate("data_consulta").toLocalDate(),
                    rs.getTime("hora_consulta").toLocalTime(),
                    rs.getString("observacao"),
                    rs.getString("diagnostico"),
                    rs.getString("cpf_paciente"),
                    rs.getString("cpf_medico")
                );
            }
        }
        return null;
    }

    public void atualizarConsulta(Consulta consulta) throws SQLException {
        String sql = "UPDATE consulta SET id_triagem = ?, sala = ?, data_consulta = ?, hora_consulta = ?, observacao = ?, diagnostico = ?, cpf_paciente = ?, cpf_medico = ? WHERE id_consulta = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, consulta.getIdTriagem());
            ps.setInt(2, consulta.getSala());
            ps.setDate(3, java.sql.Date.valueOf(consulta.getDataConsulta()));
            ps.setTime(4, java.sql.Time.valueOf(consulta.getHoraConsulta()));
            ps.setString(5, consulta.getObservacao());
            ps.setString(6, consulta.getDiagnostico());
            ps.setString(7, consulta.getCpfPaciente());
            ps.setString(8, consulta.getCpfMedico());
            ps.setInt(9, consulta.getIdConsulta());

            ps.executeUpdate();
        }
    }

    public void deletarConsulta(int idConsulta) throws SQLException {
        String sql = "DELETE FROM consulta WHERE id_consulta = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            ps.executeUpdate();
        }
    }
}
