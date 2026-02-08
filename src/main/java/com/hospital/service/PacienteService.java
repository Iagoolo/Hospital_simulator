package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hospital.dao.PacienteDAO;
import com.hospital.model.Paciente;

public class PacienteService {
    private Connection connection;

    public PacienteService(Connection connection){
        this.connection = connection;
    }

    /**
     * Cadastra um novo paciente no banco de dados.
     * 
     * Este método recebe um objeto {@link Paciente} contendo os detalhes do paciente a ser cadastrado.
     * A operação de inserção é realizada dentro de uma transação SQL para garantir a integridade
     * dos dados. Se a operação for bem-sucedida, a transação é confirmada; caso contrário, é
     * realizada uma reversão (rollback) para manter o estado consistente do banco de dados.
     * 
     * @param paciente O objeto {@link Paciente} contendo os detalhes do paciente a ser cadastrado.
     * @throws SQLException Se ocorrer um erro ao cadastrar o paciente no banco de dados.
     */
    public void cadastrarPaciente(Paciente paciente) throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);

        try{
            connection.setAutoCommit(false);
            pacienteDAO.add(paciente);
            connection.commit();

            System.out.println("Paciente cadastrado com sucesso!");
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar paciente. Revertendo situação.");

            try{
                connection.rollback();
            } catch (SQLException ex){
                System.err.println("Erro ao reverter situação: " + ex.getMessage());
            }

            throw new SQLException("Erro ao registrar paciente: " + e.getMessage());
        }
    }

    /**
     * Atualiza os dados de um paciente no banco de dados.
     * 
     * Este método recebe um objeto {@link Paciente} com os dados atualizados e
     * realiza a atualização no banco de dados utilizando a classe {@link PacienteDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * @param paciente
     * @throws SQLException
     */
    public void atualizar(Paciente paciente) throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);

        try {
            connection.setAutoCommit(false);
            pacienteDAO.atualizar(paciente);
            connection.commit();

            System.out.println("Paciente atualizado com sucesso");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar paciente. Revertendo situação.");

            try {
                connection.rollback();
            } catch (SQLException ex){
                System.err.println("Erro ao reverter situação: " + ex.getMessage());
            }

            throw new SQLException("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    /**
     * Deleta um paciente do banco de dados com base no CPF fornecido.
     * 
     * Este método realiza a exclusão do paciente utilizando a classe {@link PacienteDAO}.
     * A operação é realizada dentro de uma transação SQL para garantir a integridade dos dados.
     * @param cpf
     * @throws SQLException
     */
    public void deletarPaciente(String cpf) throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);

        try {
            connection.setAutoCommit(false);
            pacienteDAO.deletar(cpf);
            connection.commit();

            System.out.println("Paciente deletado com sucesso!");
        } catch (SQLException e){
            
            try{
                connection.rollback();
            } catch (SQLException ex){
                System.err.println("Erro ao reverter deletação de Paciente: " + ex.getMessage());
            }

            throw new SQLException("Erro ao deletar paciente: " + e.getMessage());
        }
    }

    /**
     * Busca um paciente no banco de dados pelo seu CPF.
     * 
     * Este método utiliza a classe {@link PacienteDAO} para buscar e retornar
     * um objeto {@link Paciente} com base no CPF fornecido.
     * @param cpf
     * @return
     * @throws SQLException
     */
    public Paciente buscarPacienteCpf(String cpf) throws SQLException {
        PacienteDAO pacienteDAO = new PacienteDAO(connection);
        return pacienteDAO.buscarPorCpf(cpf);
    }

    /**
     * Lista todos os pacientes cadastrados no banco de dados.
     * 
     * Este método utiliza a classe {@link PacienteDAO} para buscar e retornar
     * uma lista de todos os objetos {@link Paciente} cadastrados.
     * @return
     * @throws SQLException
     */
    public List<Paciente> listarTodosPacientes() throws SQLException{
        PacienteDAO pacienteDAO = new PacienteDAO(connection);
        return pacienteDAO.listarTodos();
    }
}
