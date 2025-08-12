package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO extends PessoaDAO<model.Paciente> {

    public PacienteDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void add(model.Paciente paciente) throws SQLException {
        String sqlPessoa = "INSERT INTO pessoa (nome, nome_pai, nome_mae, endereco, CPF, idade) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPaciente = "INSERT INTO paciente (cpf_paciente) VALUES (?)";
        String sqlSintoma = "INSERT INTO paciente_sintomas (cpf_paciente, sintoma) VALUES (?, ?)";

        try{
            connection.setAutoCommit(false);

            try (PreparedStatement psPessoa = connection.prepareStatement(sqlPessoa)) {
                psPessoa.setString(1, paciente.getNome());
                psPessoa.setString(2, paciente.getNomePai());
                psPessoa.setString(3, paciente.getNomeMae());
                psPessoa.setString(4, paciente.getEndereco());
                psPessoa.setString(5, paciente.getCpf());
                psPessoa.setInt(6, paciente.getIdade());
                psPessoa.executeUpdate();
            }

            try (PreparedStatement psPaciente = connection.prepareStatement(sqlPaciente)) {
                psPaciente.setString(1, paciente.getCpf());
                psPaciente.executeUpdate();
            }

            try (PreparedStatement psSintoma = connection.prepareStatement(sqlSintoma)) {
                for (String sintoma : paciente.getSintomas()) {
                    psSintoma.setString(1, paciente.getCpf());
                    psSintoma.setString(2, sintoma);
                    psSintoma.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error adding paciente: " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public List<model.Paciente> listarTodos() throws SQLException {
        List<model.Paciente> pacientes = new ArrayList<>();
        String sql = """
                SELECT * FROM paciente
                INNER JOIN pessoa ON paciente.cpf_paciente = pessoa.cpf
                LEFT JOIN Paciente_sintomas ON paciente.cpf_paciente = Paciente_sintomas.cpf_paciente
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.Paciente paciente = new model.Paciente();

                paciente.setNome(rs.getString("nome"));
                paciente.setNomePai(rs.getString("nomePai"));
                paciente.setNomeMae(rs.getString("nomeMae"));
                paciente.setEndereco(rs.getString("endereco"));
                paciente.setCpf(rs.getString("cpf"));

                String sintoma = rs.getString("sintoma");
                if (sintoma != null) {
                    paciente.getSintomas().add(sintoma);
                }

                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            throw new SQLException("Error listing pacientes: " + e.getMessage(), e);
        }

        return pacientes;
    }

    @Override
    public model.Paciente buscarPorCpf(String cpf) throws SQLException {
        String sql = """
                SELECT * FROM paciente
                INNER JOIN pessoa ON paciente.cpf_paciente = pessoa.cpf
                LEFT JOIN paciente_sintomas ON paciente.cpf_paciente = paciente_sintomas.cpf_paciente
                WHERE paciente.cpf_paciente = ?
                """;

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.Paciente paciente = new model.Paciente();

                paciente.setNome(rs.getString("nome"));
                paciente.setNomePai(rs.getString("nome_pai"));
                paciente.setNomeMae(rs.getString("nome_mae"));
                paciente.setEndereco(rs.getString("endereco"));
                paciente.setCpf(rs.getString("cpf_paciente"));

                String sintoma = rs.getString("sintomas");
                if (sintoma != null) {
                    paciente.getSintomas().add(sintoma);
                }

                return paciente;
            }
        } catch (SQLException e) {
            throw new SQLException("Error searching paciente: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public void atualizar(model.Paciente paciente) throws SQLException{
        String sql = "UPDATE paciente SET nome = ?, endereco = ?, nomePai = ?, nomeMae = ? WHERE cpf = ? ";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getEndereco());
            ps.setString(3, paciente.getNomePai());
            ps.setString(4, paciente.getNomeMae());
            ps.setString(5, paciente.getCpf());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(String cpf) throws SQLException {
        String sql = "DELETE FROM paciente WHERE cpf_paciente = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Paciente não encontrado");
            }
        } catch (SQLException e) {
            throw new SQLException("Error deleting paciente: " + e.getMessage(), e);
        }
    }
}
