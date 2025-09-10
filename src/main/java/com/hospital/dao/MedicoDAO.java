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
