package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Sala;

public class SalaDAO {
    
    private Connection connection;

    public SalaDAO (Connection connection){
        this.connection = connection;
    }

    public void addSala(Sala sala) throws SQLException {
        String sql = "INSERT INTO sala (andar, tipo_sala) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, sala.getAndar());
            ps.setString(2, sala.getTipoSala());
            ps.executeUpdate();
        }
    }

    public void deletarSala(int idSala) throws SQLException{
        String sql = "DELETE FROM sala WHERE id_sala = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idSala);
            ps.executeUpdate();
        }
    }

    public void atualizarSala(Sala sala) throws SQLException{
        String sql = "UPDATE sala SET andar = ?, tipo_sala = ? WHERE id_sala = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, sala.getAndar());
            ps.setString(2, sala.getTipoSala());
            ps.setInt(3, sala.getIdSala());

            ps.executeUpdate();
        }
    }

    public List<Sala> listarAllSalas() throws SQLException{
        String sql = "SELECT * FROM sala";

        List<Sala> salas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                salas.add(new Sala(
                    rs.getInt("id_sala"),
                    rs.getInt("andar"),
                    rs.getString("tipo_sala")
                ));
            }
        }

        return salas;
    }

    public Sala buscarSala(int idSala) throws SQLException{
        String sql = "SELECT * FROM sala WHERE id_sala = ?";

        Sala sala = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idSala);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                sala = new Sala(
                    rs.getInt("id_sala"),
                    rs.getInt("andar"),
                    rs.getString("tipo_sala")
                );
            }
        }

        return sala;
    }
}
