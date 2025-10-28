package com.midoribank.atm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/midoribank?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do MySQL não encontrado!");
            throw new RuntimeException("Driver não encontrado", e);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            System.err.println("URL: " + DB_URL);
            System.err.println("Usuário: " + DB_USER);
            throw new RuntimeException("Falha na conexão com o DB", e);
        }
    }
}