package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.MedicamentosDAO;
import model.Medicamento;

public class MedicamentosService {
    private Connection connection;

    public MedicamentosService(Connection connection){
        this.connection = connection;
    }

    public Medicamento cadastrarMedicamentos(Medicamento medicamento) throws SQLException{
        MedicamentosDAO medicamentosDAO = new MedicamentosDAO(connection);

        try {
            connection.setAutoCommit(false);;
            medicamentosDAO.add(medicamento);
            connection.commit();

            System.out.println("Medicamento cadastrado com sucesso!");
            return medicamento;
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao cadastrar medicamento: " + e.getMessage());
        }
    }

    public void deletarMedicamento(int idMedicamento) throws SQLException, Exception{
        MedicamentosDAO medicamentosDAO = new MedicamentosDAO(connection);

        if (medicamentosDAO.isMedicamentoEmUso(idMedicamento)){
            throw new Exception("Medicamento em uso, impossível deletar");
        }
        try {
            connection.setAutoCommit(false);
            medicamentosDAO.deletar(idMedicamento);
            connection.commit();

            System.out.println("Medicamento deletado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar medicamento: " + e.getMessage());
        }
    }

    public void atualizarMedicamento(Medicamento medicamento) throws SQLException{
        MedicamentosDAO medicamentosDAO = new MedicamentosDAO(connection);

        try {
            connection.setAutoCommit(false);
            medicamentosDAO.update(medicamento);
            connection.commit();

            System.out.println("Medicamento atualizado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao atualizar medicamento: " + e.getMessage());
        }
    }

    public Medicamento buscarMedicamento(int idMedicamento) throws SQLException{
        MedicamentosDAO medicamentosDAO = new MedicamentosDAO(connection);
        return medicamentosDAO.buscar(idMedicamento);
    }

    public List<Medicamento> listarMedicamentos() throws SQLException{
        MedicamentosDAO medicamentosDAO = new MedicamentosDAO(connection);
        return medicamentosDAO.listar();
    }
}
