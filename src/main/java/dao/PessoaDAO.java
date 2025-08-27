package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class PessoaDAO<T> {
    
    protected final Connection connection;

    public PessoaDAO (Connection connection){
        this.connection = connection;
    }

    public abstract void add(T entity) throws SQLException;
    public abstract T buscarPorCpf(String cpf) throws SQLException;
    public abstract List<T> listarTodos() throws SQLException;
    public abstract void atualizar(T entity) throws SQLException;
    public abstract void deletar(String cpf) throws SQLException;
}
