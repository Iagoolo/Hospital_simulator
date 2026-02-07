package com.hospital.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hospital.model.Medico;

public class MedicoDAO extends PessoaDAO<Medico>{

    public MedicoDAO(Connection connection) {
        super(connection);
    }

    /**
     * Adiciona um novo médico ao banco de dados.
     * 
     * <p>Este método insere um médico no sistema através de três operações:
     * <ul>
     *   <li>Insere os dados pessoais na tabela Pessoa</li>
     *   <li>Insere os dados específicos do médico na tabela Medico</li>
     *   <li>Insere as especializações do médico na tabela Medico_Especializacao</li>
     * </ul>
     * </p>
     * 
     * @param medico o objeto {@link Medico} contendo os dados do médico a ser adicionado
     * @throws SQLException se ocorrer um erro ao acessar ou modificar o banco de dados
     * 
     * @see Medico
     */
    @Override
    public void add(Medico medico) throws SQLException {
        
        String sqlPessoa = "INSERT INTO Pessoa (CPF, Nome, Endereco, Idade, Nome_pai, Nome_mae) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psPessoa = connection.prepareStatement(sqlPessoa)) {
            psPessoa.setString(1, medico.getCpf());
            psPessoa.setString(2, medico.getNome());
            psPessoa.setString(3, medico.getEndereco());
            psPessoa.setInt(4, medico.getIdade());
            psPessoa.setString(5, medico.getNomePai());
            psPessoa.setString(6, medico.getNomeMae());
            psPessoa.executeUpdate();
        }
        
        String sqlMedico = "INSERT INTO Medico (CPF_medico, Turno) VALUES (?, ?)";
        try (PreparedStatement psMedico = connection.prepareStatement(sqlMedico)) {
            psMedico.setString(1, medico.getCpf());
            psMedico.setString(2, medico.getTurno());
            psMedico.executeUpdate();
        }
        
        String sqlEspec = "INSERT INTO Medico_Especializacao (CPF_medico, Especializacao) VALUES (?, ?)";
        try (PreparedStatement psEspec = connection.prepareStatement(sqlEspec)) {
            for (String especializacao : medico.getEspecialidades()) {
                psEspec.setString(1, medico.getCpf());
                psEspec.setString(2, especializacao);
                psEspec.addBatch();
            }

            psEspec.executeBatch();
        }
    }

    
    /**
     * Busca um médico no banco de dados pelo número de CPF.
     * 
     * <p>Este método realiza uma consulta que une as tabelas de médico, pessoa e
     * especializações, retornando todas as informações do médico incluindo suas
     * especialidades.</p>
     * 
     * @param cpf o número de CPF do médico a ser buscado
     * @return um objeto {@link Medico} contendo os dados do médico e suas especialidades,
     *         ou {@code null} se nenhum médico for encontrado com o CPF fornecido
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados
     * 
     * @see Medico
     */
    @Override
    public Medico buscarPorCpf(String cpf) throws SQLException {
        String sql = 
                """
                SELECT * 
                FROM
                    medico
                INNER JOIN
                    pessoa ON medico.cpf_medico = pessoa.cpf
                LEFT JOIN
                    medico_especializacao ON medico.cpf_medico = medico_especializacao.cpf_medico
                WHERE medico.cpf_medico = ?
                """;

        Map<String, Medico> medicoMap = new java.util.HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                Medico medico = medicoMap.get(cpf);

                if (medico == null){
                    medico = new Medico();
                    medico.setCpf(cpf);
                    medico.setNome(rs.getString("Nome"));
                    medico.setEndereco(rs.getString("Endereco"));
                    medico.setIdade(rs.getInt("Idade"));
                    medico.setNomePai(rs.getString("Nome_pai"));
                    medico.setNomeMae(rs.getString("Nome_mae"));
                    medico.setTurno(rs.getString("turno"));

                    medicoMap.put(cpf, medico);
                }

                String especializacao = rs.getString("especializacao");
                if (especializacao != null){
                    medico.addEspecialidade(especializacao);
                }
            }
        }

        return medicoMap.get(cpf);
    }

    
    /**
     * Lista todos os médicos cadastrados no sistema.
     * 
     * <p>Este método recupera todos os médicos do banco de dados, juntamente com
     * seus dados pessoais e especializações associadas. A consulta realiza:
     * <ul>
     *   <li>INNER JOIN com a tabela pessoa para obter dados pessoais</li>
     *   <li>LEFT JOIN com a tabela medico_especializacao para obter especializações</li>
     * </ul>
     * 
     * <p>O método utiliza um mapa para evitar duplicação de registros de médicos
     * quando há múltiplas especializações associadas ao mesmo CPF.
     * 
     * @return uma lista contendo todos os médicos cadastrados com seus respectivos
     *         dados pessoais e especializações
     * @throws SQLException se ocorrer um erro ao executar a consulta no banco de dados
     */
    @Override
    public List<Medico> listarTodos() throws SQLException {
        String sql = 
                """
                SELECT * 
                FROM 
                    medico
                INNER JOIN 
                    pessoa ON medico.cpf_medico = pessoa.cpf
                LEFT JOIN 
                    medico_especializacao ON medico.cpf_medico = medico_especializacao.cpf_medico
                """;

        Map<String, Medico> medicoMap = new java.util.HashMap<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String medicoCpf = rs.getString("CPF_medico");
                Medico medico = medicoMap.get(medicoCpf);

                if (medico == null){
                    medico = new Medico();
                    medico.setNome(rs.getString("nome"));
                    medico.setCpf(rs.getString("cpf_medico"));
                    medico.setTurno(rs.getString("turno"));
                    medico.setIdade(rs.getInt("idade"));
                    medico.setNomePai(rs.getString("nome_pai"));
                    medico.setNomeMae(rs.getString("nome_mae"));
                    medico.setEndereco(rs.getString("endereco"));

                    medicoMap.put(medicoCpf, medico);
                }

                String especializacao = rs.getString("especializacao");
                if (especializacao != null) {
                    medico.addEspecialidade(especializacao);
                }
            }
        } 

        return new ArrayList<>(medicoMap.values());
    }

    
    /**
     * Atualiza as informações de um médico no banco de dados.
     * 
     * Este método realiza a atualização de dados pessoais, informações específicas do médico
     * e suas especializações. O processo é dividido em quatro etapas:
     * <ol>
     *   <li>Atualiza dados pessoais na tabela Pessoa (nome, endereço, idade, nomes dos pais)</li>
     *   <li>Atualiza o turno na tabela Medico</li>
     *   <li>Remove todas as especializações anteriores do médico</li>
     *   <li>Insere as novas especializações em lote</li>
     * </ol>
     * 
     * @param medico O objeto {@link Medico} contendo as informações atualizadas do médico.
     *               Deve conter CPF válido para identificar o registro a ser atualizado.
     * 
     * @throws SQLException Se ocorrer um erro durante a execução de qualquer uma das
     *                      operações no banco de dados.
     * 
     * @see Medico
     */
    @Override
     public void atualizar(Medico medico) throws SQLException{
         
        String sqlPessoa = "UPDATE Pessoa SET Nome = ?, Endereco = ?, Idade = ?, Nome_pai = ?, Nome_mae = ? WHERE CPF = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlPessoa)) {
            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getEndereco());
            ps.setInt(3, medico.getIdade());
            ps.setString(4, medico.getNomePai());
            ps.setString(5, medico.getNomeMae());
            ps.setString(6, medico.getCpf());
            ps.executeUpdate();
        }

        String sqlMedico = "UPDATE Medico SET Turno = ? WHERE CPF_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlMedico)) {
            ps.setString(1, medico.getTurno());
            ps.setString(2, medico.getCpf());
            ps.executeUpdate();
        }

        String sqlDelEsp = "DELETE FROM Medico_especializacao WHERE CPF_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlDelEsp)){
            ps.setString(1, medico.getCpf());
            ps.executeUpdate();
        }

        String sqlInsEsp = "INSERT INTO Medico_especializacao (CPF_medico, Especializacao) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlInsEsp)){
            for (String especializacao : medico.getEspecialidades()) {
                ps.setString(1, medico.getCpf());
                ps.setString(2, especializacao);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    /**
     * Deleta um médico do sistema, removendo todas as suas associações.
     * 
     * Este método realiza a exclusão em cascata do médico especificado,
     * removendo primeiro suas especializações, depois os dados do médico
     * e por fim os dados pessoais.
     * 
     * @param cpfMedico o CPF do médico a ser deletado
     * @throws SQLException se nenhum médico for encontrado com o CPF fornecido
     *                      ou se ocorrer algum erro durante a operação de exclusão
     *                      no banco de dados
     */
    @Override
    public void deletar(String cpfMedico) throws SQLException {
        
        String sqlEspecializacao = "DELETE FROM Medico_especializacao WHERE CPF_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlEspecializacao)){
            ps.setString(1, cpfMedico);
            ps.executeUpdate();
        }

        String sqlMedico = "DELETE FROM Medico WHERE CPF_medico = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlMedico)){
            ps.setString(1, cpfMedico);
            ps.executeUpdate();
        }

        String sqlPessoa = "DELETE FROM Pessoa WHERE CPF = ? ";
        try (PreparedStatement ps = connection.prepareStatement(sqlPessoa)){
            ps.setString(1, cpfMedico);
            
            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0){
                throw new SQLException("Erro ao deletar: Nenhum médico encontrado");
            }
        }
    }
}
