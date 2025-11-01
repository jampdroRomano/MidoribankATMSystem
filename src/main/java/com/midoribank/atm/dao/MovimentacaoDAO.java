package com.midoribank.atm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MovimentacaoDAO {

    public enum TipoMovimentacao {
        DEPOSITO,
        SAQUE,
        TRANSFERENCIA_ENVIADA,
        TRANSFERENCIA_RECEBIDA
    }

    public boolean registrarMovimentacao(Connection conn, int contaId, TipoMovimentacao tipo, double valor, Integer contaDestinoId) {

        String sql = "INSERT INTO movimentacao (conta_id, tipo_movimentacao, valor, data_hora, conta_destino_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, contaId);
            stmt.setString(2, tipo.name());
            stmt.setDouble(3, valor);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            if (contaDestinoId != null) {
                stmt.setInt(5, contaDestinoId);
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar movimentação: " + e.getMessage());
            return false;
        }
    }
}