package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hospital.model.Atendimento;

/**
 * Classe DAO para gerenciar operações de banco de dados relacionadas a atendimentos.
 */
public class AtendimentoDAO {
    
    private final Connection connection;

    public AtendimentoDAO (Connection connection){
        this.connection = connection;
    }

    /**
     * Adiciona um novo atendimento ao banco de dados.
     * @param atendimento O objeto Atendimento a ser adicionado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
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

    /**
     * Atualiza um atendimento existente no banco de dados.
     * @param atendimento O objeto Atendimento com os dados atualizados.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
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

    /**
     * Finaliza um atendimento no banco de dados.
     * @param idAtendimento O ID do atendimento a ser finalizado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    public void finalizarAtendimento(int idAtendimento) throws SQLException {
        String sql = "UPDATE atendimento SET status = ? WHERE id_atendimento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Finalizado");
            stmt.setInt(2, idAtendimento);
            stmt.executeUpdate();
        }
    }

    /**
     * Busca o próximo paciente na fila de atendimento.
     * @return O objeto Atendimento do próximo paciente ou null se não houver pacientes na fila.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    public Atendimento buscarProximoPaciente() throws SQLException {
        String sql = "SELECT * FROM atendimento WHERE status = 'Aguardando Triagem' ORDER BY hora_atendimento ASC LIMIT 1";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Atendimento atendimento = new Atendimento();
                
                atendimento.setIdAtendimento(rs.getInt("id_atendimento"));
                atendimento.setCpfPaciente(rs.getString("cpf_paciente"));
                atendimento.setStatus(rs.getString("status"));
                atendimento.setSenha(rs.getString("senha"));
                atendimento.setHoraAtendimento(rs.getTime("hora_atendimento"));
                
                atendimento.setIdConsulta((Integer) rs.getObject("id_consulta"));
                atendimento.setIdTriagem((Integer) rs.getObject("id_triagem"));
                atendimento.setIdSala((Integer) rs.getObject("id_sala"));
                
                return atendimento;
            }
        }
        return null;
    }

    /**
     * Busca o próximo atendimento para consulta.
     * @return O objeto Atendimento do próximo paciente para consulta ou null se não houver pacientes na fila.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    public Atendimento buscarProximoParaConsulta() throws SQLException {
        String sql = 
                """
                    SELECT a.*
                    FROM atendimento a
                    LEFT JOIN 
                    triagem t ON a.id_triagem = t.id_triagem
                    WHERE a.status = 'Aguardando Consulta'
                    ORDER BY
                        CASE
                            WHEN t.Prioridade = 'Alta' THEN 1
                            WHEN t.Prioridade = 'Média' THEN 2
                            WHEN t.Prioridade = 'Baixa' THEN 3
                            ELSE 4
                        END ASC,
                        a.hora_atendimento ASC LIMIT 1;
                """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Atendimento atendimento = new Atendimento();
                atendimento.setIdAtendimento(rs.getInt("id_atendimento"));
                atendimento.setCpfPaciente(rs.getString("cpf_paciente"));
                atendimento.setStatus(rs.getString("status"));
                atendimento.setIdTriagem((Integer) rs.getObject("id_triagem"));
                return atendimento;
            }
        }
        return null; 
    }

    /**
     * Busca um atendimento pelo seu ID.
     * @param idAtendimento O ID do atendimento a ser buscado.
     * @return O objeto Atendimento correspondente ao ID ou null se não encontrado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    public Atendimento buscarPorId(int idAtendimento) throws SQLException {
        String sql = "SELECT * FROM atendimento WHERE id_atendimento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idAtendimento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Atendimento atendimento = new Atendimento();
                    atendimento.setIdAtendimento(rs.getInt("id_atendimento"));
                    atendimento.setCpfPaciente(rs.getString("cpf_paciente"));
                    atendimento.setStatus(rs.getString("status"));
                    atendimento.setIdTriagem((Integer) rs.getObject("id_triagem"));
                    atendimento.setIdConsulta((Integer) rs.getObject("id_consulta"));
                    atendimento.setIdSala((Integer) rs.getObject("id_sala"));
                    return atendimento;
                }
            }
        }
        return null;
    }

    public Atendimento buscarPorConsulta(int idConsulta) throws SQLException {
        String sql = "SELECT * FROM atendimento WHERE id_consulta = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Atendimento atendimento = new Atendimento();
                    atendimento.setIdAtendimento(rs.getInt("id_atendimento"));
                    atendimento.setCpfPaciente(rs.getString("cpf_paciente"));
                    atendimento.setStatus(rs.getString("status"));
                    atendimento.setIdTriagem((Integer) rs.getObject("id_triagem"));
                    atendimento.setIdConsulta((Integer) rs.getObject("id_consulta"));
                    atendimento.setIdSala((Integer) rs.getObject("id_sala"));
                    return atendimento;
                }
            }
        }

        return null;
    }
}
