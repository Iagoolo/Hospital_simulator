package dao;

import java.sql.*;
import java.util.List;

public class EnfermeiroDAO extends PessoaDAO<model.Enfermeiro> {

    private Connection connection;

    public EnfermeiroDAO(Connection connection){
        super(connection);
    }

    @Override
    public void add(model.Enfermeiro enfermeiro) throws SQLException{
        String sql = "INSERT INTO enfermeiro (cpf_enfermeiro) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, enfermeiro.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding enfermeiro: " + e.getMessage(), e);
        }
    }

    @Override
    public model.Enfermeiro buscarPorCpf (String cpfEnfermeiro) throws SQLException{
        String sql = "SELECT * FROM enfermeiro WHERE cpf_enfermeiro = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cpfEnfermeiro);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                model.Enfermeiro enfermeiro = new model.Enfermeiro();

                enfermeiro.setCpf(rs.getString("cpf_enfermeiro"));
                enfermeiro.setNome(rs.getString("nome"));
                return enfermeiro;
            }
        } catch (SQLException e){
            throw new SQLException("Erros searching enfermeiro: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<model.Enfermeiro> listarTodos() throws SQLException{
        String sql = "SELECT * FROM enfermeiro";
        List<model.Enfermeiro> enfermeiros = new java.util.ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                model.Enfermeiro enfermeiro = new model.Enfermeiro();

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
            ps.executeUpdate();
        } catch (SQLException e){
            throw new SQLException("Error delete enfermeiro: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(model.Enfermeiro enfermeiro) throws SQLException{
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
