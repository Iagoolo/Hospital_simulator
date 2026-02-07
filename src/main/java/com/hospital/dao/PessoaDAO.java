package com.hospital.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class PessoaDAO<T> {
    
    protected final Connection connection;

    public PessoaDAO (Connection connection){
        this.connection = connection;
    }

    public abstract void add(T entity) throws SQLException; // Método para adicionar uma nova pessoa ao banco de dados
    public abstract T buscarPorCpf(String cpf) throws SQLException; // Método para buscar uma pessoa pelo CPF, retornando um objeto do tipo T
    public abstract List<T> listarTodos() throws SQLException; // Método para listar todas as pessoas do banco de dados, retornando uma lista de objetos do tipo T
    public abstract void atualizar(T entity) throws SQLException; // Método para atualizar os dados de uma pessoa existente no banco de dados, recebendo um objeto do tipo T com as informações atualizadas
    public abstract void deletar(String cpf) throws SQLException; // Método para deletar uma pessoa do banco de dados com base no CPF, recebendo o CPF como parâmetro para identificar a pessoa a ser removida
}
