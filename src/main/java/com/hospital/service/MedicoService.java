package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.MedicoDAO;
import com.hospital.model.Medico;

public class MedicoService {
    
    private Connection connection;

    public MedicoService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Cadastra um novo médico no banco de dados.
     * 
     * Este método recebe um objeto {@link Medico} contendo os detalhes do médico a ser cadastrado.
     * A operação de inserção é realizada dentro de uma transação SQL para garantir a integridade
     * dos dados. Se a operação for bem-sucedida, a transação é confirmada; caso contrário, é
     * realizada uma reversão (rollback) para manter o estado consistente do banco de dados.
     * 
     * @param medico O objeto {@link Medico} contendo os detalhes do médico a ser cadastrado.
     * @throws SQLException Se ocorrer um erro ao cadastrar o médico no banco de dados.
     */
    public void cadastrarMedico(Medico medico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            medicoDAO.add(medico);
            connection.commit();

            System.out.println("Médico cadastrado com sucesso!!");
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar médico. Revertendo situação");

            try {
                connection.rollback();
            } catch(SQLException ex){
                System.err.println("Erro ao reverter a situação: " + ex.getMessage());
            }

            throw new SQLException("Error registering medico: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza os dados de um médico no banco de dados.
     * 
     * Este método recebe um objeto {@link Medico} com os dados atualizados e
     * realiza a atualização no banco de dados utilizando a classe {@link MedicoDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * @param medico
     * @throws SQLException
     */
    public void atualizarMedico(Medico medico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);
        
        try {
            connection.setAutoCommit(false);
            medicoDAO.atualizar(medico);
            connection.commit();
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao atualizar médico", e);
        }
    }

    /**
     * Deleta um médico do banco de dados com base no CPF fornecido.
     * 
     * Este método realiza a exclusão do médico utilizando a classe {@link MedicoDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * @param cpfMedico
     * @throws SQLException
     */
    public void deletarMedico(String cpfMedico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            medicoDAO.deletar(cpfMedico);
            connection.commit();

            System.out.println("Médico deletado com sucesso!");
        } catch(SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar médico.", e);
        }
    }

    /**
     * Busca um médico no banco de dados pelo seu CPF.
     * 
     * Este método utiliza a classe {@link MedicoDAO} para buscar e retornar
     * um objeto {@link Medico} com base no CPF fornecido.
     * @param cpfMedico
     * @return
     * @throws SQLException
     */
    public Medico buscarMedicoCpf(String cpfMedico) throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);
        return medicoDAO.buscarPorCpf(cpfMedico);
    }

    /**
     * Lista todos os médicos cadastrados no banco de dados.
     * 
     * Este método utiliza a classe {@link MedicoDAO} para buscar e retornar
     * uma lista de todos os objetos {@link Medico} cadastrados.
     * @return
     * @throws SQLException
     */
    public List<Medico> listarTodosMedicos() throws SQLException{
        MedicoDAO medicoDAO = new MedicoDAO(connection);
        return medicoDAO.listarTodos();
    }
}