package com.midoribank.atm.dao;

import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.utils.CriptografiaUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    private Connection connection;
    private boolean closeConnection;

    public UserDAO() {
        this.connection = ConnectionFactory.getConnection();
        this.closeConnection = true;
    }

    public UserDAO(Connection connection) {
        this.connection = connection;
        this.closeConnection = false;
    }

    private void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    System.err.println("Erro ao fechar recurso: " + e.getMessage());
                }
            }
        }
        if (closeConnection) {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public boolean autenticar(String email, String senha) {
        String sql = "SELECT senha FROM usuario WHERE email = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String senhaHashBanco = rs.getString("senha");
                return CriptografiaUtils.checkPassword(senha, senhaHashBanco);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
        } finally {
            if (closeConnection) {
                close(rs, stmt);
            } else {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar recursos internos: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public UserProfile getProfile(String email) {
        String sql = "SELECT " +
                "  u.id, u.nome, u.email, " +
                "  c.agencia, c.numero_conta, c.saldo, " +
                "  ca.numero_cartao, ca.senha AS pin_cartao, " +
                "  (SELECT senha FROM usuario WHERE email = ?) AS senha_conta " +
                "FROM " +
                "  usuario u " +
                "JOIN conta c ON u.id = c.usuario_id " +
                "JOIN cartao ca ON c.id = ca.conta_id " +
                "WHERE " +
                "  u.email = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String senhaConta = rs.getString("senha_conta");
                String agencia = rs.getString("agencia");
                String numeroConta = rs.getString("numero_conta");
                double saldo = rs.getDouble("saldo");
                String cartao = rs.getString("numero_cartao");
                String pin = rs.getString("pin_cartao");
                return new UserProfile(id, nome, email, numeroConta, agencia, senhaConta, saldo, cartao, pin);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar perfil do usuário: " + e.getMessage());
        } finally {
            if (closeConnection) {
                close(rs, stmt);
            } else {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar recursos internos: " + e.getMessage());
                }
            }
        }
        return null;
    }

    public int cadastrarUsuario(String nome, String email, String senha) {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        try {
            String senhaHash = CriptografiaUtils.hashPassword(senha);
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senhaHash);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        } finally {
            if (closeConnection) {
                close(generatedKeys, stmt);
            } else {
                try {
                    if (generatedKeys != null) generatedKeys.close();
                    if (stmt != null) stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar recursos internos: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    public boolean verificarEmailExistente(String email) {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar e-mail: " + e.getMessage());
            return false;
        } finally {
            if (closeConnection) {
                close(rs, stmt);
            } else {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar recursos internos: " + e.getMessage());
                }
            }
        }
    }
}