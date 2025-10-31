package com.midoribank.atm.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/midoribank?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "midori";
    private static final String DB_PASSWORD = "senha_segura123";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initializeTables(connection);
            return connection;

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

    private static void initializeTables(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS `usuario` (" +
                    "`id` int NOT NULL AUTO_INCREMENT, " +
                    "`nome` varchar(100) NOT NULL, " +
                    "`email` varchar(100) NOT NULL, " +
                    "`senha` varchar(100) NOT NULL, " +
                    "PRIMARY KEY (`id`), " +
                    "UNIQUE KEY `email` (`email`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");

            stmt.execute("CREATE TABLE IF NOT EXISTS `conta` (" +
                    "`id` int NOT NULL AUTO_INCREMENT, " +
                    "`usuario_id` int NOT NULL, " +
                    "`agencia` varchar(10) NOT NULL, " +
                    "`numero_conta` varchar(20) NOT NULL, " +
                    "`saldo` decimal(10,2) NOT NULL DEFAULT '0.00', " +
                    "PRIMARY KEY (`id`), " +
                    "UNIQUE KEY `numero_conta` (`numero_conta`), " +
                    "KEY `usuario_id` (`usuario_id`), " +
                    "CONSTRAINT `conta_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");

            stmt.execute("CREATE TABLE IF NOT EXISTS `cartao` (" +
                    "`id` int NOT NULL AUTO_INCREMENT, " +
                    "`numero_cartao` varchar(16) DEFAULT NULL, " +
                    "`senha` varchar(4) DEFAULT NULL, " +
                    "`conta_id` int NOT NULL, " +
                    "PRIMARY KEY (`id`), " +
                    "UNIQUE KEY `numero_cartao` (`numero_cartao`), " +
                    "KEY `conta_id` (`conta_id`), " +
                    "CONSTRAINT `cartao_ibfk_1` FOREIGN KEY (`conta_id`) REFERENCES `conta` (`id`)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}
