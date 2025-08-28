package service;

import java.sql.Connection;
import java.sql.SQLException;

import dao.ConsultaDAO;
import model.Consulta;

public class ConsultaService {
    private Connection connection;

    public ConsultaService(Connection connection){
        this.connection = connection;
    }
    
    public void criarConsulta(Consulta consulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);
        
        try{
            connection.setAutoCommit(false);
            consultaDAO.add(consulta);
            connection.commit();

            System.out.println("Consulta criada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao criar consulta: " + e.getMessage());
        }
    }

    public void deletarConsulta(int idConsulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);
        
        try {
            connection.setAutoCommit(false);
            consultaDAO.deletar(idConsulta);
            connection.commit();

            System.out.println("Consulta deletada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao deletar consulta: " + e.getMessage());
        }
    }

    public Consulta procurarConsultaId(int idConsulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);
        return consultaDAO.buscarConsulta(idConsulta);
    }

    public void atualizarConsulta(Consulta consulta) throws SQLException{
        ConsultaDAO consultaDAO = new ConsultaDAO(connection);

        try {
            connection.setAutoCommit(false);
            consultaDAO.atualizarConsulta(consulta);
            connection.commit();

            System.out.println("Consulta atualizada com sucesso!");
        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao atualizar consulta: " + e.getMessage());
        }
    }
}
