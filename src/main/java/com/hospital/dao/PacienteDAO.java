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


                String sintoma = rs.getString("Paciente_Sintomas");
                if (sintoma != null) {
                    paciente.addSintomas(sintoma);
                }
            }
        }

        return new ArrayList<>(pacienteMap.values());
    }

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

                String sintoma = rs.getString("Paciente_Sintomas");
                if (sintoma != null) {
                    paciente.addSintomas(sintoma);;
                }
            }
        }

        return pacienteMap.get(cpf);
    }

    @Override
    public void atualizar(Paciente paciente) throws SQLException{
        
        String sqlPessoa = "UPDATE Pessoa SET nome = ?, endereco = ?, nomePai = ?, nomeMae = ? WHERE cpf = ? ";
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

    @Override
    public void deletar(String cpf) throws SQLException {
        
        String sqlSint = "DELETE FROM Paciente_sintomas WHERE CPF_paciente = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlSint)){
            ps.setString(1, cpf);
            ps.executeUpdate();
        }

        String sql = "DELETE FROM paciente WHERE cpf_paciente = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpf);
            ps.executeUpdate();
        }

        String sqlPessoa = "DELETE FROM Pessoa WHERE = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlPessoa)){
            ps.setString(1, cpf);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Paciente não encontrado");
            }
        }
    }
}
