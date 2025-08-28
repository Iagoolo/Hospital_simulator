package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Consulta;
import model.ItemPrescricao;
import model.Prescricao;

public class ConsultaDAO {
    private final Connection connection;

    public ConsultaDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(Consulta consulta) throws SQLException{
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
        SELECT 
            c.*, p.id_prescricao, ip.id_item, ip.id_medicamento, ip.dosagem, ip.frequencia, ip.duracao, ip.observacoes
        FROM 
            consulta c
        LEFT JOIN 
            prescricao p ON c.id_consulta = p.id_consulta
        LEFT JOIN 
            prescricao_item ip ON p.id_prescricao = ip.id_prescricao
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

            int idMedicamento = rs.getInt("id_medicamento");
            if (idMedicamento > 0) {
                prescricao.addItem(new ItemPrescricao(
                    rs.getInt("id_item"),
                    rs.getInt("id_prescricao"),
                    rs.getInt("id_medicamento"),
                    rs.getString("dosagem"),
                    rs.getString("frequencia"),
                    rs.getString("duracao"),
                    rs.getString("observacoes")
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

    public void deletar(int idConsulta) throws SQLException {
        String sql = "DELETE FROM consulta WHERE id_consulta = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            ps.executeUpdate();
        }
    }
}
