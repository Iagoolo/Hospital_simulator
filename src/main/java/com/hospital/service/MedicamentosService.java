package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.MedicamentosDAO;
import com.hospital.model.Medicamento;

public class MedicamentosService {
    private Connection connection;

    public MedicamentosService(Connection connection){
        this.connection = connection;
    }

    /**
     * Este método cadastra um novo medicamento no banco de dados. Ele recebe um objeto
     * {@link Medicamento} contendo os detalhes do medicamento a ser cadastrado. A operação
     * de inserção é realizada dentro de uma transação SQL para garantir a integridade dos
     * dados. Se a operação for bem-sucedida, a transação é confirmada; caso contrário, é realizada
     * uma reversão (rollback) para manter o estado consistente do banco de dados.
     * @param medicamento
     * @return
     * @throws SQLException
     */
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

    /**
     * Deleta um medicamento do banco de dados com base no ID fornecido.
     * 
     * Antes de realizar a exclusão, o método verifica se o medicamento está em uso
     * por outras entidades no sistema. Se estiver em uso, uma exceção é lançada
     * para impedir a exclusão. A operação de exclusão é realizada dentro de uma
     * transação SQL para garantir a integridade dos dados.
     * @param idMedicamento
     * @throws SQLException
     * @throws Exception
     */
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

    /**
     * Atualiza os dados de um medicamento no banco de dados.
     * 
     * Este método recebe um objeto {@link Medicamento} com os dados atualizados e
     * realiza a atualização no banco de dados utilizando a classe {@link MedicamentosDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * @param medicamento
     * @throws SQLException
     */
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

    /**
     * Busca um medicamento no banco de dados pelo seu ID.
     * 
     * Este método utiliza a classe {@link MedicamentosDAO} para buscar e retornar
     * um objeto {@link Medicamento} com base no ID fornecido.
     * @param idMedicamento
     * @return
     * @throws SQLException
     */
    public Medicamento buscarMedicamento(int idMedicamento) throws SQLException{
        MedicamentosDAO medicamentosDAO = new MedicamentosDAO(connection);
        return medicamentosDAO.buscar(idMedicamento);
    }

    /**
     * Lista todos os medicamentos cadastrados no banco de dados.
     * 
     * Este método utiliza a classe {@link MedicamentosDAO} para buscar e retornar
     * uma lista de todos os objetos {@link Medicamento} cadastrados.
     * @return
     * @throws SQLException
     */
    public List<Medicamento> listarMedicamentos() throws SQLException{
        MedicamentosDAO medicamentosDAO = new MedicamentosDAO(connection);
        return medicamentosDAO.listar();
    }
}
