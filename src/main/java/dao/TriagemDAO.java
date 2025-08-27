package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Triagem;

public class TriagemDAO {
    
    private final Connection connection;

    public TriagemDAO(Connection connection) {
        this.connection = connection;
    }

    public void addTriagem(Triagem triagem) throws SQLException{
        String sql = "INSERT INTO triagem (prioridade, data_triagem, hora_triagem, temperatura, peso, cpf_paciente, cpf_enfermeiro) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, triagem.getPrioridade());
            ps.setDate(2, java.sql.Date.valueOf(triagem.getDataTriagem()));
            ps.setTime(3, java.sql.Time.valueOf(triagem.getHoraTriagem()));
            ps.setDouble(4, triagem.getTemperatura());
            ps.setDouble(5, triagem.getPeso());
            ps.setString(6, triagem.getCpfPaciente());
            ps.setString(7, triagem.getCpfEnfermeiro());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                triagem.setIdTriagem(rs.getInt(1));
            }
        }
    }

    public Triagem buscarTriagem(int id) throws SQLException {
        String sql = "SELECT * FROM triagem WHERE id_triagem = ?";
        Triagem triagem = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                triagem = new Triagem(
                    rs.getInt("id_triagem"),
                    rs.getString("prioridade"),
                    rs.getDate("data_triagem").toLocalDate(),
                    rs.getTime("hora_triagem").toLocalTime(),
                    rs.getDouble("temperatura"),
                    rs.getDouble("peso"),
                    rs.getString("cpf_paciente"),
                    rs.getString("cpf_enfermeiro")
                );
            }
        }

        return triagem;
    }

    public List<Triagem> listarTriagens() throws SQLException {
        String sql = "SELECT * FROM triagem";
        List<Triagem> triagens = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Triagem triagem = new Triagem(
                    rs.getInt("id_triagem"),
                    rs.getString("prioridade"),
                    rs.getDate("data_triagem").toLocalDate(),
                    rs.getTime("hora_triagem").toLocalTime(),
                    rs.getDouble("temperatura"),
                    rs.getDouble("peso"),
                    rs.getString("cpf_paciente"),
                    rs.getString("cpf_enfermeiro")
                );
                triagens.add(triagem);
            }
        }

        return triagens;
    }

    public void atualizarTriagem(Triagem triagem) throws SQLException {
        String sql = "UPDATE triagem SET prioridade = ?, data_triagem = ?, hora_triagem = ?, temperatura = ?, peso = ?, cpf_paciente = ?, cpf_enfermeiro = ? WHERE id_triagem = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, triagem.getPrioridade());
            ps.setDate(2, java.sql.Date.valueOf(triagem.getDataTriagem()));
            ps.setTime(3, java.sql.Time.valueOf(triagem.getHoraTriagem()));
            ps.setDouble(4, triagem.getTemperatura());
            ps.setDouble(5, triagem.getPeso());
            ps.setString(6, triagem.getCpfPaciente());
            ps.setString(7, triagem.getCpfEnfermeiro());
            ps.setInt(8, triagem.getIdTriagem());

            ps.executeUpdate();
        }
    }
}
