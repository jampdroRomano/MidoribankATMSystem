package com.midoribank.atm.database; // O pacote do banco

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContaDAO {

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