package com.midoribank.atm.dao;

import com.midoribank.atm.models.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean autenticar(String email, String senhaConta) {

        String sql = "SELECT id FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senhaConta);

            try (ResultSet rs = stmt.executeQuery()) {

                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public UserProfile getProfile(String email) {

        String sql = "SELECT " +
                     "  u.nome, u.senha, " +
                     "  c.agencia, c.numero_conta, c.saldo, " +
                     "  ca.numero_cartao, ca.senha AS pin_cartao " +
                     "FROM " +
                     "  usuario u " +
                     "JOIN conta c ON u.id = c.usuario_id " +
                     "JOIN cartao ca ON c.id = ca.conta_id " +
                     "WHERE " +
                     "  u.email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String nome = rs.getString("nome");
                    String senhaConta = rs.getString("senha");
                    String agencia = rs.getString("agencia");
                    String numeroConta = rs.getString("numero_conta");
                    double saldo = rs.getDouble("saldo");
                    String cartao = rs.getString("numero_cartao");
                    String pin = rs.getString("pin_cartao");

                    return new UserProfile(nome, numeroConta, agencia, senhaConta, saldo, cartao, pin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar perfil do usuário: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
