package com.midoribank.atm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContaDAO {

    private Connection connection;

    public ContaDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public ContaDAO(Connection connection) {
        this.connection = connection;
    }

    public int cadastrarConta(int usuarioId, String agencia, String numeroConta) {
        String sql = "INSERT INTO conta (usuario_id, agencia, numero_conta, saldo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, usuarioId);
            stmt.setString(2, agencia);
            stmt.setString(3, numeroConta);
            stmt.setDouble(4, 0.0);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar conta: " + e.getMessage());
        }
        return -1;
    }

    public boolean atualizarSaldo(String numeroConta, double novoSaldo) {
        String sql = "UPDATE conta SET saldo = ? WHERE numero_conta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, novoSaldo);
            stmt.setString(2, numeroConta);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar saldo no banco: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}