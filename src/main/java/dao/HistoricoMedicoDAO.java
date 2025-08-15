package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.HistoricoMedico;

public class HistoricoMedicoDAO {
    
    private Connection connection;

    public HistoricoMedicoDAO (Connection connection){
        this.connection = connection;
    }

    public void addHistorico (HistoricoMedico historico) throws SQLException{
        String sql = "INSERT INTO historico_medico (cpf_paciente, observacoes, status) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, historico.getCpfPaciente());
            ps.setString(2, historico.getObservacoes());
            ps.setString(3, historico.getStatus());
            ps.executeUpdate();
        }
    }

    public List<HistoricoMedico> listarAllHistoricos(String cpfPaciente) throws SQLException{
        String sql = "SELECT * FROM historico_medico WHERE cpf_paciente = ?";

        List<HistoricoMedico> historicos = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfPaciente);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                historicos.add(new HistoricoMedico(
                    rs.getInt("id_historico"),
                    rs.getString("cpf_paciente"),
                    rs.getString("observacoes"),
                    rs.getDate("ultima_atualizacao"),
                    rs.getString("status")
                ));
            }
        }

        return historicos;
    }

    public HistoricoMedico buscarHistoricoPorPaciente(String cpfPaciente) throws SQLException {
        String sql = "SELECT * FROM historico_medico WHERE cpf_paciente = ? AND status = 'Ativo'";

        HistoricoMedico historico = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cpfPaciente);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                historico = new HistoricoMedico(
                    rs.getInt("id_historico"),
                    rs.getString("cpf_paciente"),
                    rs.getString("observacoes"),
                    rs.getDate("ultima_atualizacao"),
                    rs.getString("status")
                );
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