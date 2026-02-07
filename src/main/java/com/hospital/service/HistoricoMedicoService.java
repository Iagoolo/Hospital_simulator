package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.hospital.dao.HistoricoMedicoDAO;
import com.hospital.model.HistoricoMedico;

public class HistoricoMedicoService {
    private Connection connection;

    public HistoricoMedicoService(Connection connection){
        this.connection = connection;
    }

    /**
     * Cadastra um histórico médico no banco de dados.
     *
     * Este método recebe um objeto {@link HistoricoMedico} e o adiciona ao banco de dados
     * utilizando a classe {@link HistoricoMedicoDAO}. O método garante que a operação
     * seja realizada dentro de uma transação, permitindo rollback em caso de erro.
     *
     * @param historico O objeto {@link HistoricoMedico} a ser cadastrado.
     * @return O objeto {@link HistoricoMedico} cadastrado.
     * @throws SQLException Se ocorrer um erro ao cadastrar o histórico médico.
     */
    public HistoricoMedico cadastrarHistorico(HistoricoMedico historico) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO = new HistoricoMedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            historicoMedicoDAO.add(historico);
            connection.commit();

            System.out.println("Histórico médico cadastrado com sucesso!");
            return historico;
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao cadastrar histórico: " + e.getMessage());
        }
    }

    /**
     * Deleta um histórico médico do banco de dados com base no CPF do paciente.
     *
     * Este método executa a operação de exclusão dentro de uma transação, garantindo
     * que a operação seja atômica. Se a exclusão for bem-sucedida, a transação é
     * confirmada. Em caso de erro, a transação é desfeita (rollback) e uma exceção
     * é lançada com detalhes do erro.
     *
     * @param cpfPaciente O CPF do paciente cujo histórico médico deve ser deletado.
     * @throws SQLException Se ocorrer um erro durante a operação de exclusão no banco de dados.
     */
    public void deletarHistorico(String cpfPaciente) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO = new HistoricoMedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            historicoMedicoDAO.deletar(cpfPaciente);
            connection.commit();

            System.out.println("Histórico deletado com sucesso!");
        } catch (SQLException e){
            connection.rollback();
            throw new SQLException("Erro ao deletar histórico: " + e.getMessage());
        }
    }

    /**
     * Atualiza as observações de um histórico médico existente no banco de dados.
     *
     * Este método recebe o ID do histórico médico a ser atualizado e as novas observações.
     * A operação é realizada dentro de uma transação, garantindo que as alterações sejam confirmadas apenas 
     * se a operação for bem-sucedida. Em caso de erro, a transação é desfeita (rollback) 
     * e uma exceção é lançada com detalhes do erro.
     * @param idHistorico O ID do histórico médico a ser atualizado.
     * @param observacoes As novas observações a serem registradas no histórico médico.
     * @throws SQLException Se ocorrer um erro ao atualizar o histórico médico no banco de dados.
     */
    public void atualizarHistorico(int idHistorico, String observacoes) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO = new HistoricoMedicoDAO(connection);

        try {
            connection.setAutoCommit(false);
            historicoMedicoDAO.atualizarObservacoes(idHistorico, observacoes);
            connection.commit();

            System.out.println("Histórico atualizado com sucesso");
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Erro ao atualizar histórico: " + e.getMessage());
        }
    }

    /**
     * Busca um histórico médico no banco de dados pelo CPF do paciente.
     *
     * Este método utiliza a classe {@link HistoricoMedicoDAO} para realizar a busca do histórico médico
     * correspondente ao CPF fornecido. Se o histórico for encontrado, ele é retornado; caso contrário,
     * uma exceção é lançada com detalhes do erro.
     *
     * @param cpf O CPF do paciente cujo histórico médico deve ser buscado.
     * @return O objeto {@link HistoricoMedico} correspondente ao CPF fornecido.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados ou se o histórico médico não for encontrado.
     */
    public HistoricoMedico buscarHistorico(String cpf) throws SQLException{
        HistoricoMedicoDAO historicoMedicoDAO =  new HistoricoMedicoDAO(connection);
        return historicoMedicoDAO.buscarHistoricoPorPaciente(cpf);
    }
}
