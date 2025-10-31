package com.midoribank.atm.dao;

import com.midoribank.atm.utils.CriptografiaUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CartaoDAO {

    public int cadastrarCartao(String numeroCartao, String cvv, String senhaCartao, int contaId, Connection conn) {
        String sql = "INSERT INTO cartao (numero_cartao, cvv, senha, conta_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String senhaHash = CriptografiaUtils.hashPassword(senhaCartao);

            stmt.setString(1, numeroCartao);
            stmt.setString(2, cvv);
            stmt.setString(3, senhaHash);
            stmt.setInt(4, contaId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar cartão: " + e.getMessage());
        }
        return -1;
    }
}