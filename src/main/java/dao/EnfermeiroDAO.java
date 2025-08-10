package dao;

import java.sql.*;
import java.util.List;
import model.Enfermeiro;

public class EnfermeiroDAO extends PessoaDAO<Enfermeiro> {

    public EnfermeiroDAO(Connection connection){
        super(connection);
    }

    @Override
    public void add(Enfermeiro enfermeiro) throws SQLException{
        String sqlPessoa = "INSERT INTO Pessoa (CPF, Nome, Endereco, Idade, Nome_pai, Nome_mae) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlEnfermeiro = "INSERT INTO enfermeiro (cpf_enfermeiro) VALUES (?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement psPessoa = connection.prepareStatement(sqlPessoa)) {
                psPessoa.setString(1, enfermeiro.getCpf());
                psPessoa.setString(2, enfermeiro.getNome());
                psPessoa.setString(3, enfermeiro.getEndereco());
                psPessoa.setInt(4, enfermeiro.getIdade());
                psPessoa.setString(5, enfermeiro.getNomePai());
                psPessoa.setString(6, enfermeiro.getNomeMae());
                psPessoa.executeUpdate();
            }

            try (PreparedStatement psEnfermeiro = connection.prepareStatement(sqlEnfermeiro)) {
                psEnfermeiro.setString(1, enfermeiro.getCpf());
                psEnfermeiro.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error adding enfermeiro: " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

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
        } catch (SQLException e){
            throw new SQLException("Erros searching enfermeiro: " + e.getMessage());
        }

        return null;
    }

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
        } catch (SQLException e){
            throw new SQLException("Erros listing enfermeiros: " + e.getMessage());
        }

        return enfermeiros;
    }

    @Override
    public void deletar(String cpf) throws SQLException{
        String sql = "DELETE FROM enfermeiro WHERE cpf_enfermeiro = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Enfermeiro não encontrado");
            }
        } catch (SQLException e){
            throw new SQLException("Error delete enfermeiro: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Enfermeiro enfermeiro) throws SQLException{
        String sql = "UPDATE enfermeiro SET nome = ? WHERE cpf_enfermeiro = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, enfermeiro.getNome());
            ps.setString(2, enfermeiro.getCpf());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error update enfermeiro: " + e.getMessage());
        }
    }
}
