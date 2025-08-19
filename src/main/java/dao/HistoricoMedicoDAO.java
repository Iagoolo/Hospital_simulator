package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Exames;
import model.HistoricoMedico;

public class HistoricoMedicoDAO {
    
    private Connection connection;

    public HistoricoMedicoDAO (Connection connection){
        this.connection = connection;
    }

    public void addHistorico (HistoricoMedico historico) throws SQLException{
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

    public List<HistoricoMedico> listarAllHistoricos(String cpfPaciente) throws SQLException{
        String sql = """
                        SELECT * 
                        FROM historico_medico
                        LEFT JOIN exames ON historico_medico.id_historico = exames.id_historico
                        WHERE cpf_paciente = ?
                     """;

        List<HistoricoMedico> historicos = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfPaciente);
            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                HistoricoMedico historico = new HistoricoMedico(
                    rs.getInt("id_historico"),
                    rs.getString("cpf_paciente"),
                    rs.getString("observacoes"),
                    rs.getDate("ultima_atualizacao"),
                    rs.getString("status_historico"),
                    new ArrayList<>()
                );

                int idExame = rs.getInt("id_exame");
                if (idExame != 0){
                    Exames exame = new Exames(
                        idExame,
                        rs.getInt("id_consulta"),
                        rs.getString("tipo"),
                        rs.getDate("solicitado_em"),
                        rs.getString("resultado"),
                        rs.getDate("data_resultado"),
                        rs.getString("status"),
                        rs.getInt("id_historico")
                    );
                    historico.getExames().add(exame);
                }
                historicos.add(historico);
            }

        }

        return historicos;
    }

    public HistoricoMedico buscarHistoricoPorPaciente(String cpfPaciente) throws SQLException {
        String sql = """
                        SELECT * 
                        FROM historico_medico
                        LEFT JOIN exames ON historico_medico.id_historico = exames.id_historico
                        WHERE cpf_paciente = ?
                     """;

        HistoricoMedico historico = null;
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                if (historico == null) {
                    historico = new HistoricoMedico(
                        rs.getInt("id_historico"),
                        rs.getString("cpf_paciente"),
                        rs.getString("observacoes"),
                        rs.getDate("ultima_atualizacao"),
                        rs.getString("status"),
                        new ArrayList<>()
                    );
                }

                int idExame = rs.getInt("id_exame");
                if (idExame != 0){
                    Exames exame = new Exames(
                        idExame,
                        rs.getInt("id_consulta"),
                        rs.getString("tipo"),
                        rs.getDate("solicitado_em"),
                        rs.getString("resultado"),
                        rs.getDate("data_resultado"),
                        rs.getString("status"),
                        rs.getInt("id_historico")
                    );
                    historico.getExames().add(exame);
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