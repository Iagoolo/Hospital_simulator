package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.hospital.model.Exames;

public class ExamesDAO {
    
    private final Connection connection;

    public ExamesDAO (Connection connection){
        this.connection = connection;
    }

    /**
     * Adiciona um novo exame ao banco de dados.
     * 
     * <p>Este método insere um novo registro na tabela de exames, associando-o
     * a uma consulta e histórico específicos. O ID do exame é gerado automaticamente
     * pelo banco de dados e atualizado no objeto Exames após a inserção.</p>
     * 
     * @param exame o objeto Exames contendo os detalhes do exame a ser adicionado.
     *              O campo idExames será atualizado com o ID gerado pelo banco de dados.
     * 
     * @throws SQLException se ocorrer um erro durante a execução da inserção
     *                      no banco de dados ou ao processar os resultados.
     * 
     * @since 1.0
     */
    public void add(Exames exame) throws SQLException{
        String sql = "INSERT INTO exames (id_consulta, id_historico, tipo, solicitado_em, resultado, data_resultado, status) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, exame.getIdConsulta());
            ps.setInt(2, exame.getIdHistorico());
            ps.setString(3, exame.getTipo());
            ps.setDate(4, new java.sql.Date(exame.getSolicitadoEm().getTime()));
            ps.setString(5, exame.getResultado());
            if (exame.getDataResultado() != null){
                ps.setDate(6, new java.sql.Date(exame.getDataResultado().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, exame.getStatus());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                exame.setIdExames(rs.getInt(1));
            }
        }
    }

    /**
     * Lista todos os exames associados a uma consulta específica.
     * 
     * <p>Este método realiza uma consulta SQL para recuperar todos os exames
     * relacionados a uma consulta identificada pelo seu ID. Os resultados são
     * mapeados para objetos Exames e retornados em uma lista.</p>
     * 
     * @param idConsulta o ID da consulta para a qual os exames devem ser listados.
     * @return uma lista de objetos Exames associados à consulta especificada.
     * @throws SQLException se ocorrer um erro durante a execução da consulta
     *                      ou ao processar os resultados.
     */
    public List<Exames> listar(int idConsulta) throws SQLException{
        String sql = "SELECT * FROM exames WHERE id_consulta = ?";

        List<Exames> exames = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idConsulta);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Exames exame = new Exames(
                    rs.getInt("id_exame"),
                    rs.getInt("id_consulta"),
                    rs.getString("tipo"),
                    rs.getDate("solicitado_em"),
                    rs.getString("resultado"),
                    rs.getDate("data_resultado"),
                    rs.getString("status"),
                    rs.getInt("id_historico")
                );
                exames.add(exame);
            }
        }

        return exames;
    }

    /**
     * Busca um exame específico pelo seu ID.
     * 
     * <p>Este método realiza uma consulta SQL para recuperar um exame com base
     * no seu ID único. Se o exame for encontrado, ele é mapeado para um objeto
     * Exames e retornado; caso contrário, o método retorna null.</p>
     * 
     * @param idExame o ID do exame a ser buscado.
     * @return um objeto Exames correspondente ao ID fornecido, ou null se não for encontrado.
     * @throws SQLException se ocorrer um erro durante a execução da consulta
     *                      ou ao processar os resultados.
     */
    public Exames buscar(int idExame) throws SQLException{
        String sql = "SELECT * FROM exames WHERE id_exame = ?";

        Exames exame = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idExame);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                exame =  new Exames(
                    rs.getInt("id_exame"),
                    rs.getInt("id_consulta"),
                    rs.getString("tipo"),
                    rs.getDate("solicitado_em"),
                    rs.getString("resultado"),
                    rs.getDate("data_resultado"),
                    rs.getString("status"),
                    rs.getInt("id_historico")
                );
            }
        }

        return exame;
    }

    /**
     * Deleta um exame do banco de dados com base no seu ID.
     * 
     * <p>Este método executa uma instrução SQL para remover um exame identificado
     * pelo seu ID. Se o exame não existir, a operação não terá efeito.</p>
     * 
     * @param idExame o ID do exame a ser deletado.
     * @throws SQLException se ocorrer um erro durante a execução da deleção
     *                      no banco de dados.
     */
    public void deletarExame(int idExame) throws SQLException{
        String sql = "DELETE FROM exames WHERE id_exame = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idExame);
            ps.executeUpdate();
        }
    }

    /**
     * Atualiza os detalhes de um exame existente no banco de dados.
     * 
     * <p>Este método executa uma instrução SQL para atualizar os campos resultado,
     * status e data_resultado de um exame identificado pelo seu ID. Se o exame
     * não existir, a operação não terá efeito.</p>
     * 
     * @param exame o objeto Exames contendo os detalhes atualizados do exame. O campo idExames deve estar definido para identificar o exame a ser atualizado.
     * @throws SQLException se ocorrer um erro durante a execução da atualização no banco de dados.
     */
    public void update(Exames exame) throws SQLException{
        String sql = "UPDATE exames SET Resultado = ?, Status = ?, data_resultado = ? WHERE id_exame = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, exame.getResultado());
            ps.setString(2, exame.getStatus());
            if (exame.getDataResultado() != null) {
                ps.setDate(3, new java.sql.Date(exame.getDataResultado().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setInt(4, exame.getIdExames());

            ps.executeUpdate();
        }
    }
}
