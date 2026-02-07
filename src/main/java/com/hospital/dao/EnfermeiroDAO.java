package com.hospital.dao;

import java.sql.*;
import java.util.List;

import com.hospital.model.Enfermeiro;

public class EnfermeiroDAO extends PessoaDAO<Enfermeiro> {

    public EnfermeiroDAO(Connection connection){
        super(connection);
    }

    /**
     * Adiciona um novo enfermeiro ao sistema, persistindo seus dados no banco de dados.
     * 
     * Este método realiza a inserção de um enfermeiro em duas etapas:
     * <ul>
     *     <li>Primeiro, insere os dados gerais da pessoa (CPF, nome, endereço, idade, nome do pai e mãe) 
     *         na tabela Pessoa</li>
     *     <li>Em seguida, insere o registro específico do enfermeiro na tabela enfermeiro, 
     *         referenciando o CPF da pessoa cadastrada</li>
     * </ul>
     * 
     * @param enfermeiro o objeto {@link Enfermeiro} contendo os dados a serem inseridos no banco de dados
     * @throws SQLException se ocorrer um erro durante a execução das operações SQL ou acesso ao banco de dados
     * 
     * @see Enfermeiro
     */
    @Override
    public void add(Enfermeiro enfermeiro) throws SQLException{
        
        String sqlPessoa = "INSERT INTO Pessoa (CPF, Nome, Endereco, Idade, Nome_pai, Nome_mae) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psPessoa = connection.prepareStatement(sqlPessoa)) {
            psPessoa.setString(1, enfermeiro.getCpf());
            psPessoa.setString(2, enfermeiro.getNome());
            psPessoa.setString(3, enfermeiro.getEndereco());
            psPessoa.setInt(4, enfermeiro.getIdade());
            psPessoa.setString(5, enfermeiro.getNomePai());
            psPessoa.setString(6, enfermeiro.getNomeMae());
            psPessoa.executeUpdate();
        }
        
        String sqlEnfermeiro = "INSERT INTO enfermeiro (cpf_enfermeiro) VALUES (?)";
        try (PreparedStatement psEnfermeiro = connection.prepareStatement(sqlEnfermeiro)) {
            psEnfermeiro.setString(1, enfermeiro.getCpf());
            psEnfermeiro.executeUpdate();
        }

    }

    /**
     * Busca um enfermeiro no banco de dados pelo seu CPF.
     *
     * <p>Este método realiza uma consulta SQL que busca um enfermeiro específico
     * utilizando seu CPF como critério de busca. A consulta faz um INNER JOIN
     * entre as tabelas 'enfermeiro' e 'pessoa' para recuperar todas as informações
     * do profissional.</p>
     *
     * @param cpfEnfermeiro o CPF do enfermeiro a ser buscado
     * @return um objeto {@link Enfermeiro} contendo os dados do enfermeiro encontrado,
     *         ou {@code null} caso nenhum enfermeiro seja encontrado com o CPF informado
     * @throws SQLException se ocorrer um erro durante a execução da consulta no banco de dados
     *
     * @see Enfermeiro
     */
    @Override
    public Enfermeiro buscarPorCpf (String cpfEnfermeiro) throws SQLException{
        String sql = """
            SELECT * 
            FROM
                enfermeiro 
            INNER JOIN
                pessoa ON enfermeiro.cpf_enfermeiro = pessoa.cpf
            WHERE cpf_enfermeiro = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfEnfermeiro);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Enfermeiro enfermeiro = new Enfermeiro();

                enfermeiro.setCpf(rs.getString("cpf_enfermeiro"));
                enfermeiro.setNome(rs.getString("nome"));
                enfermeiro.setEndereco(rs.getString("endereco"));
                enfermeiro.setIdade(rs.getInt("idade"));
                enfermeiro.setNomePai(rs.getString("nome_pai"));
                enfermeiro.setNomeMae(rs.getString("nome_mae"));

                return enfermeiro;
            }
        }

        return null;
    }

    /**
     * Lista todos os enfermeiros cadastrados no sistema.
     * 
     * <p>Este método realiza uma consulta no banco de dados para recuperar
     * todos os registros de enfermeiros, fazendo uma junção com a tabela
     * de pessoa para obter os dados completos.</p>
     * 
     * @return uma lista contendo todos os enfermeiros cadastrados. A lista
     *         pode estar vazia caso não existam enfermeiros no banco de dados.
     * 
     * @throws SQLException se ocorrer um erro durante a execução da consulta
     *                      no banco de dados ou ao processar os resultados.
     * 
     * @since 1.0
     */
    @Override
    public List<Enfermeiro> listarTodos() throws SQLException{
        String sql = """
                SELECT * FROM enfermeiro
                INNER JOIN pessoa ON enfermeiro.cpf_enfermeiro = pessoa.cpf
                """;
        
                List<Enfermeiro> enfermeiros = new java.util.ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Enfermeiro enfermeiro = new Enfermeiro();

                enfermeiro.setCpf(rs.getString("cpf_enfermeiro"));
                enfermeiro.setNome(rs.getString("nome"));
                enfermeiros.add(enfermeiro);
            }
        }

        return enfermeiros;
    }

    /**
     * Deleta um enfermeiro do sistema com base no CPF fornecido.
     *
     * <p>Este método realiza a exclusão de um enfermeiro do banco de dados. Ele
     * primeiro remove o registro correspondente na tabela 'enfermeiro' e, em seguida,
     * remove o registro associado na tabela 'Pessoa'. Se o CPF fornecido não corresponder
     * a nenhum registro, uma exceção será lançada.</p>
     *
     * @param cpf o CPF do enfermeiro a ser deletado
     * @throws SQLException se ocorrer um erro durante a execução das operações SQL ou acesso ao banco de dados
     */
    @Override
    public void deletar(String cpf) throws SQLException{

        String sqlEnfer = "DELETE FROM enfermeiro WHERE cpf_enfermeiro = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlEnfer)){
            ps.setString(1, cpf);
            ps.executeUpdate();
        }

        String sqlPessoa = "DELETE FROM Pessoa WHERE CPF = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlPessoa)){
            ps.setString(1, cpf);
        
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Enfermeiro não encontrado");
            }
        }
    }

    /**
     * Atualiza as informações de um enfermeiro existente no sistema.
     *
     * <p>Este método realiza a atualização dos dados de um enfermeiro no banco de dados. Ele
     * atualiza os campos relacionados à pessoa (nome, endereço, idade, nome do pai e mãe) na
     * tabela 'Pessoa' com base no CPF do enfermeiro fornecido. Se o CPF não corresponder a
     * nenhum registro existente, uma exceção será lançada.</p>
     *
     * @param enfermeiro o objeto {@link Enfermeiro} contendo os dados atualizados do enfermeiro
     * @throws SQLException se ocorrer um erro durante a execução da operação SQL ou acesso ao banco de dados
     */
    @Override
    public void atualizar(Enfermeiro enfermeiro) throws SQLException {
       
        String sqlPessoa = "UPDATE Pessoa SET Nome = ?, Endereco = ?, Idade = ?, Nome_pai = ?, Nome_mae = ? WHERE CPF = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlPessoa)) {
            ps.setString(1, enfermeiro.getNome());
            ps.setString(2, enfermeiro.getEndereco());
            ps.setInt(3, enfermeiro.getIdade());
            ps.setString(4, enfermeiro.getNomePai());
            ps.setString(5, enfermeiro.getNomeMae());
            ps.setString(6, enfermeiro.getCpf());
            ps.executeUpdate();
        }
    }
}
