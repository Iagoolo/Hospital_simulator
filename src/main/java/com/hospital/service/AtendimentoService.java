package com.hospital.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.hospital.dao.AtendimentoDAO;
import com.hospital.exception.FilaVaziaException;
import com.hospital.model.Atendimento;

public class AtendimentoService {
    private Connection connection;

    public AtendimentoService(Connection connection){
        this.connection = connection;
    }

    /**
     * Realiza um atendimento no sistema hospitalar.
     * 
     * <p>Este método processa um atendimento executando as seguintes etapas:</p>
     * <ul>
     *   <li>Desativa o autocommit da conexão</li>
     *   <li>Persiste o atendimento no banco de dados através do DAO</li>
     *   <li>Confirma a transação (commit)</li>
     * </ul>
     * 
     * <p>Em caso de erro durante a execução, a transação é desfeita (rollback)
     * e uma exceção é lançada com a mensagem de erro detalhada.</p>
     * 
     * @param atendimento o objeto {@link Atendimento} contendo os dados do atendimento
     *                    a ser realizado. Não deve ser nulo.
     * 
     * @return o objeto {@link Atendimento} que foi processado e persistido
     * 
     * @throws SQLException se ocorrer um erro durante a persistência do atendimento
     *                      no banco de dados ou durante o gerenciamento da transação.
     *                      A mensagem de erro incluirá detalhes da causa raiz.
     * 
     * @see AtendimentoDAO#add(Atendimento)
     * @see java.sql.Connection#setAutoCommit(boolean)
     * @see java.sql.Connection#commit()
     * @see java.sql.Connection#rollback()
     */
    public Atendimento realizarAtendimento(Atendimento atendimento) throws SQLException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);

        try {
            connection.setAutoCommit(false);
            atendimentoDAO.add(atendimento);
            connection.commit();

            return atendimento;
        } catch (SQLException e) {
            connection.rollback();

            throw new SQLException("Erro ao realizar atendimento: " + e.getMessage());
        }
    }
    

    /**
     * Atualiza um atendimento existente no banco de dados.
     * 
     * <p>Este método realiza a atualização de um atendimento através de uma transação
     * no banco de dados. Caso ocorra algum erro durante a operação, a transação é
     * desfeita (rollback) e uma exceção é lançada.</p>
     * 
     * @param atendimento o objeto {@link Atendimento} contendo os dados atualizados
     *                    do atendimento a ser persistido no banco de dados
     * 
     * @throws SQLException se ocorrer algum erro ao atualizar o atendimento no banco
     *                      de dados, incluindo mensagem descritiva do erro original
     * 
     * @see AtendimentoDAO#atualizar(Atendimento)
     * 
     * @since 1.0
     */
    public void atualizarAtendimento(Atendimento atendimento) throws SQLException {
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);

        try {
            connection.setAutoCommit(false);
            
            atendimentoDAO.atualizar(atendimento); 
            
            connection.commit();

            System.out.println("Atendimento ID " + atendimento.getIdAtendimento() + " atualizado para status: " + atendimento.getStatus());
        
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Erro ao atualizar atendimento: " + e.getMessage(), e);
        }
    }

    /**
     * Finaliza um atendimento no banco de dados.
     * 
     * Este método realiza a finalização de um atendimento identificado pelo seu ID.
     * A operação é executada dentro de uma transação, garantindo que todos os dados
     * sejam persistidos corretamente ou revertidos em caso de erro.
     * 
     * @param idAtendimento o identificador único do atendimento a ser finalizado
     * 
     * @throws SQLException se ocorrer um erro durante a finalização do atendimento
     *                      ou durante a comunicação com o banco de dados
     * 
     * @see AtendimentoDAO#finalizarAtendimento(int)
     */
    public void finalizarAtendimento(int idAtendimento) throws SQLException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);

        try {
            connection.setAutoCommit(false);
            atendimentoDAO.finalizarAtendimento(idAtendimento);
            connection.commit();

        } catch (SQLException e){
            connection.rollback();

            throw new SQLException("Erro ao finalizar atendimento: " + e.getMessage());
        }
    }
    
    /**
     * Busca o próximo paciente na fila de triagem para atendimento.
     * 
     * <p>Este método consulta a camada de dados para obter o primeiro paciente
     * aguardando na fila de triagem, respeitando a ordem de chegada.</p>
     * 
     * @return {@link Atendimento} contendo os dados do próximo paciente a ser atendido
     * @throws SQLException se ocorrer um erro na comunicação com o banco de dados
     * @throws FilaVaziaException se não houver pacientes aguardando na fila de triagem
     */
    public Atendimento buscarProximoPaciente() throws SQLException, FilaVaziaException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);
        Atendimento proximo = atendimentoDAO.buscarProximoPaciente();

        if (proximo == null){
            throw new FilaVaziaException("Não há pacientes aguardando na fila de triagem.");
        }

        return proximo;
    }

    /**
     * Busca o próximo atendimento aguardando na fila de consulta.
     * 
     * Este método consulta o banco de dados através da camada de acesso a dados (DAO)
     * para obter o próximo paciente que está aguardando para ser atendido em consulta,
     * seguindo a ordem de chegada (FIFO).
     * 
     * @return {@link Atendimento} o próximo atendimento da fila de consulta
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     * @throws Exception se não houver pacientes aguardando na fila de consulta
     */
    public Atendimento buscarProximoParaConsulta() throws SQLException, Exception {
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);
        Atendimento proximo = atendimentoDAO.buscarProximoParaConsulta();

        if (proximo == null) {
            throw new Exception("Não há pacientes aguardando na fila de consulta.");
        }
        
        return proximo;
    }

    /**
     * Busca um atendimento pelo seu identificador único.
     *
     * @param idAtendimento o identificador do atendimento a ser buscado
     * @return o objeto {@link Atendimento} correspondente ao ID fornecido
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     * @throws Exception se o atendimento com o ID especificado não for encontrado
     */
    public Atendimento buscarAtendimentoPorId(int idAtendimento) throws SQLException, Exception {
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);
        return atendimentoDAO.buscarPorConsulta(idAtendimento);
    }

    public Atendimento buscarAtendimentoPorConsulta(int idConsulta) throws SQLException{
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(connection);
        return atendimentoDAO.buscarPorConsulta(idConsulta);
    }
}
