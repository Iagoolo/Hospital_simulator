package com.hospital.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hospital.model.Paciente;

public class PacienteDAO extends PessoaDAO<Paciente> {

    public PacienteDAO(Connection connection) {
        super(connection);
    }

    /**
     * Adiciona um novo paciente ao banco de dados.
     * 
     * <p>Este método realiza a inserção de um paciente em três etapas:
     * <ol>
     *   <li>Insere os dados pessoais (nome, CPF, endereço, idade, etc.) na tabela Pessoa</li>
     *   <li>Insere o registro do paciente na tabela Paciente, referenciando o CPF</li>
     *   <li>Insere os sintomas associados ao paciente na tabela Paciente_sintomas</li>
     * </ol>
     * </p>
     * 
     * <p>A operação é executada em três prepared statements separados, cada um otimizado
     * para sua função específica. Os sintomas são inseridos em lote (batch) para melhor
     * desempenho quando há múltiplos sintomas a registrar.</p>
     * 
     * @param paciente o objeto Paciente contendo todos os dados a serem persistidos,
     *                 incluindo informações pessoais (nome, CPF, endereço, idade, pais)
     *                 e uma lista de sintomas
     * 
     * @throws SQLException se ocorrer um erro durante a execução de qualquer uma das
     *                      operações SQL, como violação de constraints ou problemas
     *                      de conexão com o banco de dados
     * 
     * @see Paciente
     * @see PreparedStatement
     */
    @Override
    public void add(Paciente paciente) throws SQLException {
        
        String sqlPessoa = "INSERT INTO pessoa (nome, nome_pai, nome_mae, endereco, CPF, idade) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psPessoa = connection.prepareStatement(sqlPessoa)) {
            psPessoa.setString(1, paciente.getNome());
            psPessoa.setString(2, paciente.getNomePai());
            psPessoa.setString(3, paciente.getNomeMae());
            psPessoa.setString(4, paciente.getEndereco());
            psPessoa.setString(5, paciente.getCpf());
            psPessoa.setInt(6, paciente.getIdade());
            psPessoa.executeUpdate();
        }
        
        String sqlPaciente = "INSERT INTO paciente (cpf_paciente) VALUES (?)";
        try (PreparedStatement psPaciente = connection.prepareStatement(sqlPaciente)) {
            psPaciente.setString(1, paciente.getCpf());
            psPaciente.executeUpdate();
        }
        
        String sqlSintoma = "INSERT INTO paciente_sintomas (cpf_paciente, sintoma) VALUES (?, ?)";
        try (PreparedStatement psSintoma = connection.prepareStatement(sqlSintoma)) {
            for (String sintoma : paciente.getSintomas()) {
                psSintoma.setString(1, paciente.getCpf());
                psSintoma.setString(2, sintoma);
                psSintoma.addBatch();
            }

            psSintoma.executeBatch();
        }
    }

    /**
     * Lista todos os pacientes registrados no sistema.
     * 
     * Este método recupera todos os pacientes do banco de dados, incluindo seus dados pessoais
     * e os sintomas associados a cada paciente. A consulta realiza um INNER JOIN com a tabela
     * de pessoas para obter as informações pessoais e um LEFT JOIN com a tabela de sintomas
     * para associar os sintomas de cada paciente.
     * 
     * @return Uma lista contendo todos os {@link Paciente} registrados no sistema.
     *         Cada paciente contém seus dados pessoais (nome, CPF, idade, endereço, etc.)
     *         e uma coleção de sintomas associados.
     * 
     * @throws SQLException Se ocorrer um erro ao executar a consulta no banco de dados.
     * 
     * @note O método utiliza um HashMap para evitar a duplicação de pacientes ao processar
     *       múltiplos registros de sintomas do mesmo paciente, garantindo que cada paciente
     *       apareça apenas uma vez na lista retornada.
     */
    @Override
    public List<Paciente> listarTodos() throws SQLException {
        String sql = 
        """
            SELECT * 
            FROM 
            paciente
            INNER JOIN 
            pessoa ON paciente.cpf_paciente = pessoa.cpf
            LEFT JOIN 
            Paciente_sintomas ON paciente.cpf_paciente = Paciente_sintomas.cpf_paciente
            """;
            
        Map<String, Paciente> pacienteMap = new java.util.HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String pacienteCpf = rs.getString("CPF_paciente");
                Paciente paciente = pacienteMap.get(pacienteCpf);

                if (paciente == null){
                    paciente = new Paciente();
                    paciente.setNome(rs.getString("nome"));
                    paciente.setNomePai(rs.getString("nome_pai"));
                    paciente.setIdade(rs.getInt("idade"));
                    paciente.setNomeMae(rs.getString("nome_mae"));
                    paciente.setEndereco(rs.getString("endereco"));
                    paciente.setCpf(rs.getString("CPF_paciente"));

                    pacienteMap.put(pacienteCpf, paciente);
                }


                String sintoma = rs.getString("sintomas");
                if (sintoma != null) {
                    paciente.addSintomas(sintoma);
                }
            }
        }

        return new ArrayList<>(pacienteMap.values());
    }

    /**
     * Busca um paciente no banco de dados utilizando seu número de CPF.
     * 
     * <p>Este método realiza uma consulta que:
     * <ul>
     * <li>Obtém os dados pessoais do paciente a partir das tabelas paciente e pessoa</li>
     * <li>Recupera todos os sintomas associados ao paciente através de um LEFT JOIN</li>
     * <li>Agrupa os sintomas em uma única instância de Paciente</li>
     * </ul>
     * </p>
     * 
     * @param cpf o número de CPF do paciente a ser buscado
     * @return um objeto {@link Paciente} contendo os dados pessoais e a lista de sintomas,
     *         ou {@code null} caso nenhum paciente seja encontrado com o CPF informado
     * @throws SQLException se ocorrer um erro durante a execução da consulta ao banco de dados
     * 
     * @since 1.0
     */
    @Override
    public Paciente buscarPorCpf(String cpf) throws SQLException {
        String sql = """
                SELECT * 
                FROM 
                    paciente
                INNER JOIN 
                    pessoa ON paciente.cpf_paciente = pessoa.cpf
                LEFT JOIN 
                    paciente_sintomas ON paciente.cpf_paciente = paciente_sintomas.cpf_paciente
                WHERE paciente.cpf_paciente = ?
                """;

        Map<String, Paciente> pacienteMap = new java.util.HashMap<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Paciente paciente = pacienteMap.get(cpf);

                if (paciente == null){
                    paciente = new Paciente();
                    paciente.setNome(rs.getString("nome"));
                    paciente.setNomePai(rs.getString("nome_pai"));
                    paciente.setNomeMae(rs.getString("nome_mae"));
                    paciente.setIdade(rs.getInt("idade"));
                    paciente.setEndereco(rs.getString("endereco"));
                    paciente.setCpf(rs.getString("cpf_paciente"));

                    pacienteMap.put(cpf, paciente);
                }

                String sintoma = rs.getString("sintomas");
                if (sintoma != null) {
                    paciente.addSintomas(sintoma);;
                }
            }
        }

        return pacienteMap.get(cpf);
    }

    /**
     * Atualiza as informações de um paciente no banco de dados.
     * 
     * Este método realiza a atualização dos dados pessoais do paciente e gerencia
     * seus sintomas associados. O processo envolve três operações principais:
     * <ol>
     *   <li>Atualiza os dados pessoais na tabela Pessoa (nome, endereço, nome do pai e nome da mãe)</li>
     *   <li>Remove todos os sintomas anteriormente associados ao paciente</li>
     *   <li>Insere os novos sintomas do paciente usando operações em lote</li>
     * </ol>
     * 
     * @param paciente O objeto {@link Paciente} contendo as informações atualizadas
     *                 a serem persistidas no banco de dados
     * 
     * @throws SQLException Se ocorrer um erro durante a execução das operações
     *                      no banco de dados, como falha de conexão, violação
     *                      de restrições ou problemas de sintaxe SQL
     * 
     * @see Paciente
     * @see PreparedStatement
     */
    @Override
    public void atualizar(Paciente paciente) throws SQLException{
        
        String sqlPessoa = "UPDATE Pessoa SET nome = ?, endereco = ?, Nome_pai = ?, Nome_mae = ? WHERE cpf = ? ";
        try (PreparedStatement ps = connection.prepareStatement(sqlPessoa)){
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getEndereco());
            ps.setString(3, paciente.getNomePai());
            ps.setString(4, paciente.getNomeMae());
            ps.setString(5, paciente.getCpf());
            ps.executeUpdate();
        }

        String sqlDelSint = "DELETE FROM Paciente_sintomas WHERE CPF_paciente = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlDelSint)){
            ps.setString(1, paciente.getCpf());
            ps.executeUpdate();
        }

        String sqlInsSint = "INSERT INTO Paciente_sintomas (CPF_paciente, Sintomas) VALUES(?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlInsSint)){
            for (String sintomas : paciente.getSintomas()){
                ps.setString(1, paciente.getCpf());
                ps.setString(2, sintomas);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    /**
     * Deleta um paciente do banco de dados através do seu CPF.
     * 
     * <p>Este método remove o paciente em cascata, deletando primeiramente
     * os registros de sintomas associados, depois o registro do paciente
     * e finalmente o registro da pessoa.</p>
     * 
     * @param cpf o CPF do paciente a ser deletado
     * @throws SQLException se o paciente não for encontrado ou se ocorrer
     *                      um erro durante a execução das operações de deleção
     */
    @Override
    public void deletar(String cpf) throws SQLException {

        String sqlPessoa = "DELETE FROM Pessoa WHERE CPF = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlPessoa)){
            ps.setString(1, cpf);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Paciente não encontrado");
            }
        }
    }
}
