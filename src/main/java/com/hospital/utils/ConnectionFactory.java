package com.hospital.utils;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.DriverManager;


public class ConnectionFactory {

    private static Properties props;

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