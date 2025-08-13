package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Consulta;
import model.ItemPrescricao;
import model.Prescricao;

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
    String sql = """
        SELECT c.*, p.id_prescricao, ip.id_item, ip.nome_medicamento, ip.dosagem, ip.frequencia, ip.duracao, ip.instrucoes
        FROM consulta c
        LEFT JOIN prescricao p ON c.id_consulta = p.id_consulta
        LEFT JOIN item_prescricao ip ON p.id_prescricao = ip.id_prescricao
        WHERE c.id_consulta = ?
    """;

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idConsulta);
        ResultSet rs = ps.executeQuery();

        Consulta consulta = null;
        Prescricao prescricao = null;

        while (rs.next()) {
            
            if (consulta == null) {
                consulta = new Consulta(
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

            int idPrescricao = rs.getInt("id_prescricao");
            if (idPrescricao > 0 && prescricao == null) {
                prescricao = new Prescricao();
                prescricao.setIdPrescricao(idPrescricao);
                prescricao.setIdConsulta(consulta.getIdConsulta());
            }

            String nomeMed = rs.getString("nome_medicamento");
            if (nomeMed != null) {
                prescricao.addItem(new ItemPrescricao(
                    rs.getInt("id_item"),
                    rs.getInt("id_medicamento"),
                    nomeMed,
                    rs.getString("dosagem"),
                    rs.getString("frequencia"),
                    rs.getString("duracao"),
                    rs.getString("instrucoes")
                ));
            }
        }

        if (consulta != null) {
            consulta.setPrescricao(prescricao);
        }

        return consulta;
    }
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
