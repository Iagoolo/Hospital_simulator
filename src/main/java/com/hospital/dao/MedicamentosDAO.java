package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hospital.model.Medicamento;

public class MedicamentosDAO {
    private final Connection connection;

    public MedicamentosDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adiciona um novo medicamento ao banco de dados.
     * 
     * <p>Este método insere um novo registro na tabela de medicamentos, associando-o
     * a uma prescrição e medicamento específicos. O ID do medicamento é gerado automaticamente
     * pelo banco de dados e atualizado no objeto Medicamento após a inserção.</p>
     * 
     * @param medicamento o objeto Medicamento contendo os detalhes do medicamento a ser adicionado. O campo idMedicamento será atualizado com o ID gerado pelo banco de dados.
     * @throws SQLException se ocorrer um erro durante a execução da inserção no banco de dados ou ao processar os resultados.
     */
    public void add(Medicamento medicamento) throws SQLException{
        String sql = "INSERT INTO medicamentos (nome, formula, forma, via_administracao) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, medicamento.getNome());
            ps.setString(2, medicamento.getFormula());
            ps.setString(3, medicamento.getForma());
            ps.setString(4, medicamento.getViaAdministracao());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                medicamento.setIdMedicamento(rs.getInt(1));
            }
        }
    }

    /**
     * Deleta um medicamento do banco de dados com base no seu ID.
     * 
     * <p>Este método executa uma instrução SQL para remover um medicamento identificado
     * pelo seu ID. Se o medicamento não existir, a operação não terá efeito.</p>
     * 
     * @param idMedicamento o ID do medicamento a ser deletado.
     * @throws SQLException se ocorrer um erro durante a execução da deleção no banco de dados.
     */
    public void deletar(int idMedicamento) throws SQLException{
        String sql = "DELETE FROM medicamentos WHERE id_medicamento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idMedicamento);
            ps.executeUpdate();
        }
    }

    /**
     * Verifica se um medicamento está em uso em alguma prescrição.
     * 
     * <p>Este método executa uma consulta SQL para verificar se existe algum item de prescrição
     * que faça referência ao medicamento identificado pelo seu ID. Ele retorna true se o medicamento
     * estiver em uso em pelo menos um item de prescrição, ou false caso contrário.</p>
     * 
     * @param idMedicamento o ID do medicamento a ser verificado.
     * @return true se o medicamento estiver em uso em alguma prescrição, ou false caso contrário.
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados ou ao processar os resultados.
     */
    public boolean isMedicamentoEmUso(int idMedicamento) throws SQLException {
        String sql = "SELECT 1 FROM prescricao_item WHERE id_medicamento = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Atualiza os detalhes de um medicamento existente no banco de dados.
     * 
     * <p>Este método executa uma instrução SQL para atualizar os campos nome, formula, forma e via_administracao
     * de um medicamento identificado pelo seu ID. Se o medicamento não existir, a operação não terá efeito.</p>
     * 
     * @param medicamento o objeto Medicamento contendo os detalhes atualizados do medicamento. O campo idMedicamento deve estar definido para identificar o medicamento a ser atualizado.
     * @throws SQLException se ocorrer um erro durante a execução da atualização no banco de dados.
     */
    public void update(Medicamento medicamento) throws SQLException{
        String sql = "UPDATE medicamentos SET nome = ?, formula = ?, forma = ?, via_administracao = ? WHERE id_medicamento = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, medicamento.getNome());
            ps.setString(2, medicamento.getFormula());
            ps.setString(3, medicamento.getForma());
            ps.setString(4, medicamento.getViaAdministracao());
            ps.setInt(5, medicamento.getIdMedicamento());
            ps.executeUpdate();
        }   
    }

    /**
     * Busca um medicamento no banco de dados com base no seu ID.
     * 
     * <p>Este método executa uma consulta SQL para recuperar um medicamento identificado pelo seu ID. Ele retorna um objeto Medicamento contendo os detalhes do medicamento, ou null se nenhum medicamento for encontrado para o ID fornecido.</p>
     * 
     * @param idMedicamento o ID do medicamento a ser buscado.
     * @return um objeto Medicamento contendo os detalhes do medicamento, ou null se nenhum medicamento for encontrado para o ID fornecido.
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados ou ao processar os resultados.
     */
    public Medicamento buscar(int idMedicamento) throws SQLException {
        String sql = "SELECT * FROM medicamentos WHERE id_medicamento = ?";
        Medicamento medicamento = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            var rs = ps.executeQuery();
            if (rs.next()) {
                medicamento = new Medicamento(
                    rs.getInt("id_medicamento"),
                    rs.getString("nome"),
                    rs.getString("formula"),
                    rs.getString("forma"),
                    rs.getString("via_administracao")
                );
            }
        }
        return medicamento;
    }

    /**
     * Lista todos os medicamentos disponíveis no banco de dados.
     * 
     * <p>Este método executa uma consulta SQL para recuperar todos os registros da tabela de medicamentos. Ele retorna uma lista de objetos Medicamento contendo os detalhes de cada medicamento encontrado.</p>
     * 
     * @return uma lista de objetos Medicamento contendo os detalhes de todos os medicamentos disponíveis no banco de dados.
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados ou ao processar os resultados.
     */
    public List<Medicamento> listar() throws SQLException {
        String sql = "SELECT * FROM medicamentos";
        List<Medicamento> medicamentos = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            var rs = ps.executeQuery();
            while (rs.next()) {
                Medicamento medicamento = new Medicamento(
                    rs.getInt("id_medicamento"),
                    rs.getString("nome"),
                    rs.getString("formula"),
                    rs.getString("forma"),
                    rs.getString("via_administracao")
                );
                medicamentos.add(medicamento);
            }
        }
        return medicamentos;
    }
}