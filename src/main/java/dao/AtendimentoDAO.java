package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Atendimento;

public class AtendimentoDAO {
    
    private Connection connection;

    public AtendimentoDAO (Connection connection){
        this.connection = connection;
    }

    public void addAtendimento(Atendimento atendimento) throws SQLException{
        String sql = "INSERT INTO Atendimento (senha, hota_atendimento, status, cpf_paciente) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, atendimento.getSenha());
            ps.setTime(2, atendimento.getHoraAtendimento());
            ps.setString(3, atendimento.getStatus());
            ps.setObject(4, atendimento.getCpfPaciente());
            ps.executeUpdate();
        }
    }

    public void atualizarSalaEStatus(int idAtendimento, int idSala, String status) throws SQLException {
        String sql = "UPDATE atendimento SET id_sala = ?, status = ? WHERE id_atendimento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idSala);
            stmt.setString(2, status);
            stmt.setInt(3, idAtendimento);
            stmt.executeUpdate();
        }
    }

    public void finalizarAtendimento(int idAtendimento) throws SQLException {
        String sql = "UPDATE atendimento SET status = ? WHERE id_atendimento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Finalizado");
            stmt.setInt(2, idAtendimento);
            stmt.executeUpdate();
        }
    }

    public Atendimento buscarProximoPaciente() throws SQLException {
        String sql = "SELECT * FROM atendimento WHERE status = 'Aguardando triagem' ORDER BY senha ASC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Atendimento atendimento = new Atendimento();
                atendimento.setIdAtendimento(rs.getInt("id_atendimento"));
                atendimento.setCpfPaciente(rs.getString("cpf_paciente"));
                atendimento.setSenha(rs.getString("senha"));
                atendimento.setStatus(rs.getString("status"));
                return atendimento;
            }
        }
        return null;
    }
}
