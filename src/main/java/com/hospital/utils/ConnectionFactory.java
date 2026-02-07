package com.hospital.utils;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.DriverManager;

// Classe utilitária para criar conexões com o banco de dados usando as propriedades definidas em db.properties
public class ConnectionFactory {

    private static Properties props;

    // Bloco estático para carregar as propriedades do banco de dados quando a classe for carregada
    static{
        props = new Properties();
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")){

            if (input == null){
                throw new RuntimeException("Não foi possível encontrar o arquivo db.properties no classpath");
            }
            
            props.load(input);
        } catch (IOException i){
            throw new RuntimeException("Erro ao abrir arquivo de propriedades do banco de dados: " + i.getMessage());
        }
    }

    // Método para criar e retornar uma nova conexão com o banco de dados usando as propriedades carregadas
    public static Connection createConnection(){
        try {
            return DriverManager.getConnection(
                props.getProperty("db.url"), 
                props.getProperty("db.user"), 
                props.getProperty("db.password")
                );
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao acessar banco de dados" + e.getMessage());
        }
    }
}