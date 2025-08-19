package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Exames;

public class ExamesDAO {
    
    private Connection connection;

    public ExamesDAO (Connection connection){
        this.connection = connection;
    }

    public void addExame(Exames exame) throws SQLException{
        String sql = "INSERT INTO exames (id_consulta, id_historico, tipo, solicitado_em, resultado, data_resultado, status) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, exame.getIdConsulta());
            ps.setInt(2, exame.getIdHistorico());
            ps.setString(3, exame.getTipo());
            ps.setDate(4, new java.sql.Date(exame.getSolicitadoEm().getTime()));
            ps.setString(5, exame.getResultado());
            if (exame.getDataResultado() != null){
                ps.setDate(6, new java.sql.Date(exame.getDataResultado().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, exame.getStatus());
            ps.executeUpdate();
        }
    }

    public List<Exames> listarAllExames(int idConsulta) throws SQLException{
        String sql = "SELECT * FROM exames WHERE id_consulta = ?";

        List<Exames> exames = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idConsulta);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Exames exame = new Exames(
                    rs.getInt("id_exame"),
                    rs.getInt("id_consulta"),
                    rs.getString("tipo"),
                    rs.getDate("solicitado_em"),
                    rs.getString("resultado"),
                    rs.getDate("data_resultado"),
                    rs.getString("status"),
                    rs.getInt("id_historico")
                );
                exames.add(exame);
            }
        }

        return exames;
    }

    public Exames buscarExame(int idExame) throws SQLException{
        String sql = "SELECT * FROM exames WHERE id_exame = ?";

        Exames exame = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idExame);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                exame =  new Exames(
                    rs.getInt("id_exame"),
                    rs.getInt("id_consulta"),
                    rs.getString("tipo"),
                    rs.getDate("solicitado_em"),
                    rs.getString("resultado"),
                    rs.getDate("data_resultado"),
                    rs.getString("status"),
                    rs.getInt("id_historico")
                );
            }
        }

        return exame;
    }

    public void deletarExame(int idExame) throws SQLException{
        String sql = "DELETE FROM exames WHERE id_exame = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idExame);
            ps.executeUpdate();
        }
    }
}
