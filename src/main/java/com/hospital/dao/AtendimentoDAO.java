package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hospital.model.Atendimento;

public class AtendimentoDAO {
    
    private final Connection connection;

    public AtendimentoDAO (Connection connection){
        this.connection = connection;
    }

    public void add(Atendimento atendimento) throws SQLException{
        String sql = "INSERT INTO Atendimento (senha, hora_atendimento, status, cpf_paciente) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, atendimento.getSenha());
            ps.setTime(2, atendimento.getHoraAtendimento());
            ps.setString(3, atendimento.getStatus());
            ps.setString(4, atendimento.getCpfPaciente());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    atendimento.setIdAtendimento(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Atendimento atendimento) throws SQLException {
        String sql = "UPDATE atendimento SET status = ?, id_triagem = ?, id_consulta = ?, id_sala = ? WHERE id_atendimento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, atendimento.getStatus());
            ps.setObject(2, atendimento.getIdTriagem());
            ps.setObject(3, atendimento.getIdConsulta());
            ps.setObject(4, atendimento.getIdSala());
            ps.setInt(5, atendimento.getIdAtendimento());

            ps.executeUpdate();
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

    public Atendimento buscarProximoParaConsulta() throws SQLException {
        String sql = "SELECT * FROM atendimento WHERE status = 'Aguardando Consulta' ORDER BY hora_atendimento ASC LIMIT 1";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Atendimento atendimento = new Atendimento();
                atendimento.setIdAtendimento(rs.getInt("id_atendimento"));
                atendimento.setCpfPaciente(rs.getString("cpf_paciente"));
                atendimento.setStatus(rs.getString("status"));
                atendimento.setIdTriagem(rs.getInt("id_triagem"));
                return atendimento;
            }
        }
        return null; 
    }
}
