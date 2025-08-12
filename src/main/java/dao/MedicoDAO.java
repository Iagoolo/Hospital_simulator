package dao;

import java.sql.*;
import java.util.List;
import model.Medico;

public class MedicoDAO extends PessoaDAO<Medico>{

    public MedicoDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void add(Medico medico) throws SQLException {
        String sqlPessoa = "INSERT INTO Pessoa (CPF, Nome, Endereco, Idade, Nome_pai, Nome_mae) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlMedico = "INSERT INTO Medico (CPF_medico, Turno) VALUES (?, ?)";
        String sqlEspec = "INSERT INTO Medico_Especializacao (CPF_medico, Especializacao) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false); 

            try (PreparedStatement psPessoa = connection.prepareStatement(sqlPessoa)) {
                psPessoa.setString(1, medico.getCpf());
                psPessoa.setString(2, medico.getNome());
                psPessoa.setString(3, medico.getEndereco());
                psPessoa.setInt(4, medico.getIdade());
                psPessoa.setString(5, medico.getNomePai());
                psPessoa.setString(6, medico.getNomeMae());
                psPessoa.executeUpdate();
            }

            try (PreparedStatement psMedico = connection.prepareStatement(sqlMedico)) {
                psMedico.setString(1, medico.getCpf());
                psMedico.setString(2, medico.getTurno());
                psMedico.executeUpdate();
            }

            try (PreparedStatement psEspec = connection.prepareStatement(sqlEspec)) {
                for (String especializacao : medico.getEspecialidades()) {
                    psEspec.setString(1, medico.getCpf());
                    psEspec.setString(2, especializacao);
                    psEspec.executeUpdate();
                }
            }

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error adding medico: " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Medico buscarPorCpf(String cpf) throws SQLException {
        String sql = """
                SELECT * 
                FROM
                    medico
                INNER JOIN
                    pessoa ON medico.cpf_medico = pessoa.cpf
                LEFT JOIN
                    medico_especializacao ON medico.cpf_medico = medico_especializacao.cpf_medico
                WHERE medico.cpf_medico = ?
        """;
        
        Medico medico = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);


           ResultSet rs = ps.executeQuery();
           while (rs.next()){
               if (medico == null) {
                   medico = new Medico();
                   medico.setNome(rs.getString("nome"));
                   medico.setCpf(rs.getString("cpf_medico"));
                   medico.setTurno(rs.getString("turno"));
                   medico.setIdade(rs.getInt("idade"));
                   medico.setNomePai(rs.getString("nome_pai"));
                   medico.setNomeMae(rs.getString("nome_mae"));
                   medico.setEndereco(rs.getString("endereco"));
               }

               String esp = rs.getString("especializacao");
               if (esp != null) {
                   medico.addEspecialidade(esp);
               }
           }
        } catch (SQLException e) {
            throw new SQLException("Error searching medico: " + e.getMessage(), e);
        }

        return medico;
    }

    @Override
    public List<Medico> listarTodos() throws SQLException {
        String sql = """
                SELECT * FROM medico
                INNER JOIN pessoa ON medico.cpf_medico = pessoa.cpf
                LEFT JOIN medico_especializacao ON medico.cpf_medico = medico_especializacao.cpf_medico
                """;
        List<Medico> medicos = new java.util.ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Medico medico = new Medico();

                medico.setNome(rs.getString("nome"));
                medico.setCpf(rs.getString("cpf_medico"));
                medico.setTurno(rs.getString("turno"));
                medico.setIdade(rs.getInt("idade"));
                medico.setNomePai(rs.getString("nome_pai"));
                medico.setNomeMae(rs.getString("nome_mae"));
                medico.setEndereco(rs.getString("endereco"));

                String esp = rs.getString("especializacao");
                if (esp != null) {
                    medico.addEspecialidade(esp);
                }
                medicos.add(medico);
            }
        } catch (SQLException e) {
            throw new SQLException("Error searching list medico: " + e.getMessage(), e);
        }

        return medicos;
    }

    @Override
     public void atualizar(Medico medico) throws SQLException{
        String sql = "UPDATE medico SET nome = ?, turno = ? WHERE cpf_medico = ? ";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getTurno());
            ps.setString(3, medico.getCpf());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating medico: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(String cpfMedico) throws SQLException {
        String sql = "DELETE FROM medico WHERE cpf_medico = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfMedico);
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Médico não encontrado");
            }
        } catch (SQLException e) {
            throw new SQLException("Error deleting medico: " + e.getMessage(), e);
        }
    }
}
