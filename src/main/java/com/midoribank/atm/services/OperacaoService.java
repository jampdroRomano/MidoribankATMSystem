package com.midoribank.atm.services;

import com.midoribank.atm.dao.ConnectionFactory;
import com.midoribank.atm.dao.ContaDAO;
import com.midoribank.atm.dao.MovimentacaoDAO;
import com.midoribank.atm.models.UserProfile;
import java.sql.Connection;
import java.sql.SQLException;

public class OperacaoService {

    private final ContaDAO contaDAO;
    private final MovimentacaoDAO movimentacaoDAO;

    public OperacaoService() {
        this.contaDAO = new ContaDAO();
        this.movimentacaoDAO = new MovimentacaoDAO();
    }

    public boolean executarSaque(UserProfile user, double valor) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            double novoSaldo = user.getSaldo() - valor;

            boolean saldoAtualizado = contaDAO.atualizarSaldo(user.getNumeroConta(), novoSaldo, conn);

            boolean movimentacaoRegistrada = movimentacaoDAO.registrarMovimentacao(
                    conn,
                    user.getContaId(),
                    MovimentacaoDAO.TipoMovimentacao.SAQUE,
                    valor,
                    null
            );

            if (saldoAtualizado && movimentacaoRegistrada) {
                conn.commit();
                return true;
            } else {
                throw new SQLException("Falha ao registrar saque, revertendo.");
            }

        } catch (SQLException e) {
            System.err.println("Erro na transação de saque: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
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

    public boolean executarDeposito(UserProfile user, double valor) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            double novoSaldo = user.getSaldo() + valor;

            boolean saldoAtualizado = contaDAO.atualizarSaldo(user.getNumeroConta(), novoSaldo, conn);

            boolean movimentacaoRegistrada = movimentacaoDAO.registrarMovimentacao(
                    conn,
                    user.getContaId(),
                    MovimentacaoDAO.TipoMovimentacao.DEPOSITO,
                    valor,
                    null
            );

            if (saldoAtualizado && movimentacaoRegistrada) {
                conn.commit();
                return true;
            } else {
                throw new SQLException("Falha ao registrar depósito, revertendo.");
            }

        } catch (SQLException e) {
            System.err.println("Erro na transação de depósito: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
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