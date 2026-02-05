package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.hospital.model.Consulta;
import com.hospital.model.ItemPrescricao;
import com.hospital.model.Prescricao;

public class ConsultaDAO {
    private final Connection connection;

    public ConsultaDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adiciona uma nova consulta ao banco de dados.
     * 
     * Este método insere um novo registro na tabela de consultas com os dados fornecidos
     * no objeto {@link Consulta}. Após a inserção, o ID gerado para a consulta é definido
     * no próprio objeto {@code consulta}.
     * 
     * @param consulta O objeto {@link Consulta} contendo os dados da nova consulta a ser adicionada.
     *                 Os campos obrigatórios devem estar preenchidos.
     * 
     * @throws SQLException Se ocorrer um erro durante a execução da consulta SQL
     *                      ou durante a comunicação com o banco de dados.
     * 
     * @see Consulta
     */
    public void add(Consulta consulta) throws SQLException{
        String sql = "INSERT INTO consulta (id_triagem, id_sala, data_consulta, hora_consulta, observacao, diagnostico, cpf_paciente, cpf_medico) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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

    /**
     * Busca uma consulta pelo seu identificador único.
     * 
     * Este método recupera uma consulta específica do banco de dados através do seu ID,
     * incluindo todas as prescrições e itens de prescrição associados. A consulta é construída
     * utilizando LEFT JOIN para buscar relacionados mesmo que a prescrição não exista.
     * 
     * <p>O método executa uma query que:
     * <ul>
     *   <li>Busca os dados da consulta (id, triagem, sala, data, hora, observação, diagnóstico, CPF do paciente e médico)</li>
     *   <li>Recupera as prescrições vinculadas à consulta</li>
     *   <li>Obtém todos os itens de prescrição com informações dos medicamentos (dosagem, frequência, duração e observações)</li>
     * </ul>
     * </p>
     * 
     * <p>Os dados são mapeados para objetos de domínio:
     * <ul>
     *   <li>{@link Consulta} - contém as informações principais da consulta</li>
     *   <li>{@link Prescricao} - armazena a prescrição associada à consulta</li>
     *   <li>{@link ItemPrescricao} - representa cada medicamento prescrito com seus detalhes</li>
     * </ul>
     * </p>
     * 
     * @param idConsulta o identificador único da consulta a ser buscada
     * @return um objeto {@link Consulta} contendo todos os dados da consulta, prescrição e itens,
     *         ou {@code null} se nenhuma consulta for encontrada com o ID fornecido
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados ou processar o resultado
     */
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
                        rs.getInt("id_sala"),
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

    
    /**
     * Lista todas as consultas que ainda não possuem diagnóstico registrado.
     * 
     * Recupera do banco de dados todas as consultas cujo campo de diagnóstico
     * está vazio (nulo ou string vazia), indicando que ainda não foram diagnosticadas.
     * 
     * @return uma {@link List} contendo objetos {@link Consulta} sem diagnóstico.
     *         A lista pode estar vazia se não houver consultas sem diagnóstico.
     * 
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados durante
     *                      a execução da consulta SQL.
     */
    public List<Consulta> listarConsultasSemDiagnostico() throws SQLException {
       
        String sql = "SELECT * FROM consulta WHERE diagnostico IS NULL OR diagnostico = ''";
        List<Consulta> consultas = new java.util.ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Consulta c = new Consulta();
                c.setIdConsulta(rs.getInt("id_consulta"));
                c.setCpfPaciente(rs.getString("cpf_paciente"));
                c.setCpfMedico(rs.getString("cpf_medico"));
                c.setDataConsulta(rs.getDate("data_consulta").toLocalDate());
                c.setHoraConsulta(rs.getTime("hora_consulta").toLocalTime());
                consultas.add(c);
            }
        }
        return consultas;
    }
    
    /**
     * Atualiza os dados de uma consulta existente no banco de dados.
     * 
     * Este método executa uma operação de atualização na tabela de consultas,
     * modificando todos os campos da consulta identificada pelo seu ID.
     * Os campos atualizados incluem: triagem associada, sala de atendimento,
     * data e hora da consulta, observações, diagnóstico, CPF do paciente e CPF do médico.
     * 
     * @param consulta O objeto {@link Consulta} contendo os dados atualizados.
     *                 Deve incluir o ID da consulta a ser atualizada e todos os
     *                 campos que serão modificados.
     * 
     * @throws SQLException Se ocorrer um erro durante a execução da consulta SQL
     *                      ou durante a comunicação com o banco de dados.
     * 
     * @see Consulta
     */
    public void atualizarConsulta(Consulta consulta) throws SQLException {
        String sql = "UPDATE consulta SET id_triagem = ?, id_sala = ?, data_consulta = ?, hora_consulta = ?, observacao = ?, diagnostico = ?, cpf_paciente = ?, cpf_medico = ? WHERE id_consulta = ?";
        
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

    /**
     * Deleta uma consulta do banco de dados pelo seu ID.
     * 
     * Este método remove um registro de consulta da tabela 'consulta' baseado no 
     * identificador único (id_consulta) fornecido. A operação é realizada diretamente 
     * no banco de dados através de uma consulta SQL DELETE.
     * 
     * @param idConsulta o identificador único da consulta a ser deletada
     * @throws SQLException se ocorrer um erro ao executar a operação no banco de dados,
     *                      como conexão perdida, erro de sintaxe SQL ou violação de 
     *                      restrições de integridade referencial
     * 
     * @since 1.0
     */
    public void deletar(int idConsulta) throws SQLException {
        String sql = "DELETE FROM consulta WHERE id_consulta = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            ps.executeUpdate();
        }
    }
}
