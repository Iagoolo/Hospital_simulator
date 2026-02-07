package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hospital.model.Exames;
import com.hospital.model.HistoricoMedico;

public class HistoricoMedicoDAO {
    
    private final Connection connection;

    public HistoricoMedicoDAO (Connection connection){
        this.connection = connection;
    }

    /**
     * Adiciona um novo histórico médico ao banco de dados.
     * 
     * <p>Este método insere um novo registro na tabela de histórico médico, associando-o
     * a um paciente específico. O ID do histórico é gerado automaticamente pelo banco de dados
     * e atualizado no objeto HistoricoMedico após a inserção.</p>
     * 
     * @param historico o objeto HistoricoMedico contendo os detalhes do histórico a ser adicionado. O campo idHistorico será atualizado com o ID gerado pelo banco de dados.
     * @throws SQLException se ocorrer um erro durante a execução da inserção no banco de dados ou ao processar os resultados.
     */
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

    /**
     * Deleta o histórico médico de um paciente do banco de dados com base no CPF do paciente.
     * 
     * <p>Este método executa uma instrução SQL para remover o histórico médico associado a um paciente identificado pelo seu CPF. Se o histórico não existir, a operação não terá efeito.</p>
     * 
     * @param cpfPaciente o CPF do paciente cujo histórico médico deve ser deletado.
     * @throws SQLException se ocorrer um erro durante a execução da deleção no banco de dados.
     */
    public void deletar(String cpfPaciente) throws SQLException{
        String sql = "DELETE FROM historico_medico WHERE CPF_paciente = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfPaciente);
            ps.executeUpdate();
        }
    }

    /**
     * Busca o histórico médico de um paciente com base no CPF do paciente.
     * 
     * <p>Este método executa uma consulta SQL para recuperar o histórico médico associado a um paciente identificado pelo seu CPF. Ele retorna um objeto HistoricoMedico contendo os detalhes do histórico, incluindo uma lista de exames relacionados. Se nenhum histórico for encontrado para o CPF fornecido, o método retornará null.</p>
     * 
     * @param cpfPaciente o CPF do paciente cujo histórico médico deve ser buscado.
     * @return um objeto HistoricoMedico contendo os detalhes do histórico médico do paciente, ou null se nenhum histórico for encontrado.
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados ou ao processar os resultados.
     */
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

    /**
     * Atualiza as observações de um histórico médico existente no banco de dados.
     * 
     * <p>Este método executa uma instrução SQL para atualizar o campo de observações de um histórico médico identificado pelo seu ID. Ele também atualiza a data da última atualização para o momento em que a alteração foi feita. Se o histórico não existir, a operação não terá efeito.</p>
     * 
     * @param idHistorico o ID do histórico médico a ser atualizado.
     * @param observacoes as novas observações a serem definidas para o histórico médico.
     * @throws SQLException se ocorrer um erro durante a execução da atualização no banco de dados.
     */
    public void atualizarObservacoes(int idHistorico, String observacoes) throws SQLException {
        String sql = "UPDATE historico_medico SET observacoes = ?, ultima_atualizacao = CURRENT_TIMESTAMP WHERE id_historico = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, observacoes);
            ps.setInt(2, idHistorico);
            ps.executeUpdate();
        }
    }
}