package com.midoribank.atm.services;

import com.midoribank.atm.dao.CartaoDAO;
import com.midoribank.atm.dao.ConnectionFactory;
import com.midoribank.atm.dao.ContaDAO;
import com.midoribank.atm.dao.UserDAO;
import java.sql.Connection;
import java.sql.SQLException;

public class CadastroService {

    public boolean realizarCadastroCompleto(String nome, String email, String senhaConta,
                                            String agencia, String numeroConta,
                                            String numeroCartao, String cvv, String senhaCartao) {

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            UserDAO userDAO = new UserDAO(conn);
            ContaDAO contaDAO = new ContaDAO(conn);
            CartaoDAO cartaoDAO = new CartaoDAO(conn);

            int usuarioId = userDAO.cadastrarUsuario(nome, email, senhaConta);
            if (usuarioId == -1) {
                throw new SQLException("Falha ao cadastrar usuário.");
            }

            int contaId = contaDAO.cadastrarConta(usuarioId, agencia, numeroConta, 0.0);
            if (contaId == -1) {
                throw new SQLException("Falha ao cadastrar conta.");
            }

            int cartaoId = cartaoDAO.cadastrarCartao(numeroCartao, cvv, senhaCartao, contaId);
            if (cartaoId == -1) {
                throw new SQLException("Falha ao cadastrar cartão.");
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro na transação de cadastro: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao reverter transação: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}