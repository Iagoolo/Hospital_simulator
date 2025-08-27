package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class ConnectionFactory {

    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public Connection createConnection(){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}