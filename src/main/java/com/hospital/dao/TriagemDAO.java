package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hospital.model.Triagem;

public class TriagemDAO {
    
    private final Connection connection;

    public TriagemDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adiciona um novo registro de triagem ao banco de dados.
     * 
     * Este método insere os dados de uma triagem na tabela 'triagem',
     * incluindo informações sobre prioridade, data, hora, sinais vitais
     * e referências aos pacientes e enfermeiros envolvidos.
     * 
     * @param triagem objeto {@link Triagem} contendo os dados a serem inseridos
     *                 - prioridade: nível de prioridade do atendimento
     *                 - dataTriagem: data em que a triagem foi realizada
     *                 - horaTriagem: hora em que a triagem foi realizada
     *                 - temperatura: temperatura corporal do paciente
     *                 - peso: peso do paciente em quilogramas
     *                 - cpfPaciente: CPF do paciente sendo triado
     *                 - cpfEnfermeiro: CPF do enfermeiro que realizou a triagem
     * 
     * @throws SQLException se ocorrer um erro ao executar a operação no banco de dados
     * 
     * @note O ID da triagem é gerado automaticamente pelo banco de dados
     *       e será atribuído ao objeto triagem após a inserção bem-sucedida
     */
    public void add(Triagem triagem) throws SQLException{
        String sql = "INSERT INTO triagem (prioridade, data_triagem, hora_triagem, temperatura, peso, cpf_paciente, cpf_enfermeiro) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, triagem.getPrioridade());
            ps.setDate(2, java.sql.Date.valueOf(triagem.getDataTriagem()));
            ps.setTime(3, java.sql.Time.valueOf(triagem.getHoraTriagem()));
            ps.setDouble(4, triagem.getTemperatura());
            ps.setDouble(5, triagem.getPeso());
            ps.setString(6, triagem.getCpfPaciente());
            ps.setString(7, triagem.getCpfEnfermeiro());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                triagem.setIdTriagem(rs.getInt(1));
            }
        }
    }

    /**
     * Busca uma triagem no banco de dados pelo seu identificador.
     *
     * @param id o identificador único da triagem a ser buscada
     * @return um objeto {@link Triagem} contendo os dados da triagem encontrada,
     *         ou {@code null} caso nenhuma triagem com o id informado seja encontrada
     * @throws SQLException se ocorrer um erro ao executar a consulta no banco de dados
     */
    public Triagem buscarTriagem(int id) throws SQLException {
        String sql = "SELECT * FROM triagem WHERE id_triagem = ?";
        Triagem triagem = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                triagem = new Triagem(
                    rs.getInt("id_triagem"),
                    rs.getString("prioridade"),
                    rs.getDate("data_triagem").toLocalDate(),
                    rs.getTime("hora_triagem").toLocalTime(),
                    rs.getDouble("temperatura"),
                    rs.getDouble("peso"),
                    rs.getString("cpf_paciente"),
                    rs.getString("cpf_enfermeiro")
                );
            }
        }

        return triagem;
    }

    /**
     * Lista todas as triagens registradas no banco de dados.
     *
     * @return uma {@link List} contendo todos os objetos {@link Triagem} cadastrados.
     *         A lista pode estar vazia se nenhuma triagem for encontrada.
     *
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados,
     *                      como problemas de conexão ou execução da consulta SQL.
     *
     * @since 1.0
     */
    public List<Triagem> listarTriagens() throws SQLException {
        String sql = "SELECT * FROM triagem";
        List<Triagem> triagens = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Triagem triagem = new Triagem(
                    rs.getInt("id_triagem"),
                    rs.getString("prioridade"),
                    rs.getDate("data_triagem").toLocalDate(),
                    rs.getTime("hora_triagem").toLocalTime(),
                    rs.getDouble("temperatura"),
                    rs.getDouble("peso"),
                    rs.getString("cpf_paciente"),
                    rs.getString("cpf_enfermeiro")
                );
                triagens.add(triagem);
            }
        }

        return triagens;
    }

    /**
     * Atualiza os dados de uma triagem existente no banco de dados.
     *
     * @param triagem objeto {@link Triagem} contendo os dados atualizados da triagem.
     *                 Deve conter todos os campos necessários para a atualização,
     *                 incluindo o ID para identificar qual triagem será atualizada.
     *
     * @throws SQLException se ocorrer um erro ao executar a operação de atualização
     *                      no banco de dados, como falha de conexão ou erro na
     *                      sintaxe SQL.
     *
     * @see Triagem
     */
    public void atualizar(Triagem triagem) throws SQLException {
        String sql = "UPDATE triagem SET prioridade = ?, data_triagem = ?, hora_triagem = ?, temperatura = ?, peso = ?, cpf_paciente = ?, cpf_enfermeiro = ? WHERE id_triagem = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, triagem.getPrioridade());
            ps.setDate(2, java.sql.Date.valueOf(triagem.getDataTriagem()));
            ps.setTime(3, java.sql.Time.valueOf(triagem.getHoraTriagem()));
            ps.setDouble(4, triagem.getTemperatura());
            ps.setDouble(5, triagem.getPeso());
            ps.setString(6, triagem.getCpfPaciente());
            ps.setString(7, triagem.getCpfEnfermeiro());
            ps.setInt(8, triagem.getIdTriagem());

            ps.executeUpdate();
        }
    }

    /**
     * Deleta um registro de triagem do banco de dados.
     *
     * @param id o identificador único da triagem a ser deletada (id_triagem)
     * @throws SQLException se ocorrer um erro durante a execução da operação
     *                      no banco de dados
     */
    public void deletar(int id) throws SQLException{
        String sql = "DELETE FROM triagem WHERE id_triagem = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Verifica se um registro de triagem está em uso em outras tabelas do sistema.
     * 
     * Este método consulta as tabelas de atendimento e consulta para determinar
     * se o id da triagem fornecido está associado a algum registro nessas tabelas.
     * 
     * @param id o identificador da triagem a ser verificado
     * @return {@code true} se a triagem está em uso em alguma das tabelas 
     *         (atendimento ou consulta), {@code false} caso contrário
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     */
    public boolean isTriagemEmUso(int id) throws SQLException{
        String sql = """
                    SELECT 1 
                    FROM 
                        atendimento 
                    WHERE id_triagem = ? 
                    UNION ALL 
                    SELECT 1 
                    FROM 
                        consulta 
                    WHERE id_triagem = ? LIMIT 1
                    """;
                    
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.setInt(3, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }
}
