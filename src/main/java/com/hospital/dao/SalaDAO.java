package com.hospital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hospital.model.Sala;

public class SalaDAO {
    
    private final Connection connection;

    public SalaDAO (Connection connection){
        this.connection = connection;
    }

    /**
     * Adiciona uma nova sala ao banco de dados.
     * 
     * <p>Este método insere um novo registro na tabela de salas, associando-o
     * a um andar e tipo de sala específicos. O ID da sala é gerado automaticamente
     * pelo banco de dados e atualizado no objeto Sala após a inserção.</p>
     * 
     * @param sala o objeto Sala contendo os detalhes da sala a ser adicionada. O campo idSala será atualizado com o ID gerado pelo banco de dados.
     * @throws SQLException se ocorrer um erro durante a execução da inserção no banco de dados ou ao processar os resultados.
     */
    public void add(Sala sala) throws SQLException {
        String sql = "INSERT INTO sala (andar, tipo_sala) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, sala.getAndar());
            ps.setString(2, sala.getTipoSala());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                sala.setIdSala(rs.getInt(1));
            }
        }
    }

    /**
     * Deleta uma sala do banco de dados pelo seu identificador.
     *
     * @param idSala o identificador único da sala a ser deletada
     * @throws SQLException se ocorrer um erro durante a execução da operação no banco de dados
     */
    public void deletar(int idSala) throws SQLException{
        String sql = "DELETE FROM sala WHERE id_sala = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idSala);
            ps.executeUpdate();
        }
    }

    /**
     * Atualiza os dados de uma sala no banco de dados.
     * 
     * @param sala objeto {@link Sala} contendo os dados a serem atualizados.
     *             Deve conter: andar, tipoSala e idSala preenchidos.
     * 
     * @throws SQLException se ocorrer um erro ao executar a operação no banco de dados
     * 
     * @see Sala
     */
    public void atualizar(Sala sala) throws SQLException{
        String sql = "UPDATE sala SET andar = ?, tipo_sala = ? WHERE id_sala = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, sala.getAndar());
            ps.setString(2, sala.getTipoSala());
            ps.setInt(3, sala.getIdSala());

            ps.executeUpdate();
        }
    }

    /**
     * Lista todas as salas cadastradas no banco de dados.
     * 
     * @return uma {@link List} contendo todos os objetos {@link Sala} recuperados da tabela sala.
     *         A lista pode estar vazia caso não existam salas cadastradas.
     * @throws SQLException se ocorrer um erro ao executar a consulta SQL ou ao acessar o banco de dados
     */
    public List<Sala> listar() throws SQLException{
        String sql = "SELECT * FROM sala";

        List<Sala> salas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                salas.add(new Sala(
                    rs.getInt("id_sala"),
                    rs.getInt("andar"),
                    rs.getString("tipo_sala")
                ));
            }
        }

        return salas;
    }

    /**
     * Busca uma sala no banco de dados pelo seu identificador.
     *
     * @param idSala o identificador único da sala a ser procurada
     * @return uma instância de {@link Sala} contendo os dados da sala encontrada,
     *         ou {@code null} caso nenhuma sala seja encontrada com o id fornecido
     * @throws SQLException se ocorrer um erro ao executar a consulta no banco de dados
     */
    public Sala buscarSala(int idSala) throws SQLException{
        String sql = "SELECT * FROM sala WHERE id_sala = ?";

        Sala sala = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idSala);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                sala = new Sala(
                    rs.getInt("id_sala"),
                    rs.getInt("andar"),
                    rs.getString("tipo_sala")
                );
            }
        }

        return sala;
    }

    /**
     * Verifica se uma sala está em uso no momento.
     * 
     * Este método consulta o banco de dados para determinar se a sala especificada
     * está sendo utilizada em alguma consulta ou atendimento ativo.
     * 
     * @param idSala o identificador da sala a ser verificada
     * @return {@code true} se a sala está em uso (existe consulta ou atendimento associado),
     *         {@code false} caso contrário
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     */
    public boolean isSalaEmUso(int idSala) throws SQLException {
        String sql = "SELECT 1 FROM consulta WHERE id_sala = ? UNION ALL SELECT 1 FROM atendimento WHERE id_sala = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idSala);
            ps.setInt(2, idSala);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }

    /**
     * Método responsável por verificado no BD salas que não possuêm status de finalizado ou cancelado
     * - salas livres
     * 
     * @return Lista com todas as salas disponíveis para atendimento
     * @throws SQLException
     */
    public List<Sala> todasLivreSalas() throws SQLException{
        String sql = """
                SELECT s.id_sala, s.Andar, s.Tipo_sala
                FROM Sala s
                LEFT JOIN Atendimento a
                    ON s.id_sala = a.id_sala
                    AND a.status NOT IN ('Finalizado', 'Cancelado')
                WHERE a.id_atendimento IS NULL
                ORDER BY s.Andar, s.id_sala
                """;

        List<Sala> salas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                salas.add(new Sala(
                    rs.getInt("id_sala"),
                    rs.getInt("Andar"),
                    rs.getString("Tipo_sala")
                ));
            }

        }
        
        return salas;
    }
}
