package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Exames;
import model.HistoricoMedico;

public class HistoricoMedicoDAO {
    
    private final Connection connection;

    public HistoricoMedicoDAO (Connection connection){
        this.connection = connection;
    }

    public void add(HistoricoMedico historico) throws SQLException{
        String sql = "INSERT INTO historico_medico (cpf_paciente, observacoes, status_historico) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, historico.getCpfPaciente());
            ps.setString(2, historico.getObservacoes());
            ps.setString(3, historico.getStatusHistorico());
            ps.executeUpdate(); 

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                historico.setIdHistorico(rs.getInt(1));
            }
        }
    }

    public void deletar(String cpfPaciente) throws SQLException{
        String sql = "DELETE FROM historico_medico WHERE CPF_paciente = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfPaciente);
            ps.executeUpdate();
        }
    }

    public HistoricoMedico buscarHistoricoPorPaciente(String cpfPaciente) throws SQLException {
        String sql = """
                SELECT hm.*, e.*
                FROM historico_medico hm
                LEFT JOIN exames e ON hm.id_historico = e.id_historico
                WHERE hm.cpf_paciente = ?
                """;

        HistoricoMedico historico = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cpfPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (historico == null) {
                    historico = new HistoricoMedico();
                    historico.setIdHistorico(rs.getInt("id_historico"));
                    historico.setCpfPaciente(rs.getString("cpf_paciente"));
                    historico.setObservacoes(rs.getString("observacoes"));
                    historico.setStatusHistorico(rs.getString("status_historico"));
                    java.sql.Timestamp ts = rs.getTimestamp("ultima_atualizacao");
                    if (ts != null) {
                        historico.setUltimaAtualizacao(new java.sql.Date(ts.getTime()));
                    }
                }

                int idExame = rs.getInt("id_exame");
                if (idExame > 0) {
                    
                    Exames exame = new Exames();
                    exame.setIdExames(idExame);
                    exame.setIdConsulta(rs.getInt("id_consulta"));
                    exame.setIdHistorico(rs.getInt("id_historico"));
                    exame.setTipo(rs.getString("Tipo"));
                    exame.setResultado(rs.getString("Resultado"));
                    exame.setStatus(rs.getString("Status"));
                    exame.setSolicitadoEm(rs.getDate("Solicitado_em"));
                    exame.setDataResultado(rs.getDate("Data_resultado"));

                    historico.addExame(exame);
                }
            }
        }
        return historico;
    }

    public void atualizarObservacoes(int idHistorico, String observacoes) throws SQLException {
        String sql = "UPDATE historico_medico SET observacoes = ?, ultima_atualizacao = CURRENT_TIMESTAMP WHERE id_historico = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, observacoes);
            ps.setInt(2, idHistorico);
            ps.executeUpdate();
        }
    }
}