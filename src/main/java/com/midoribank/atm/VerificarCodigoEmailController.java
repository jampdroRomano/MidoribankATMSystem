package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VerificarCodigoEmailController {

    @FXML
    private TextField codigoField; // Dê este fx:id ao seu campo de código

    @FXML
    private Label errorLabel; // Dê este fx:id a um Label de erro

    @FXML
    private void handleVerificarCodigo() throws IOException {
        String codigo = codigoField.getText();
        
        // --- SIMULAÇÃO ---
        String codigoCorreto = "123456"; // Este seria o código salvo no banco

        // 1. Validação
        if (codigo.isEmpty()) {
            errorLabel.setText("Por favor, digite o código de 6 dígitos.");
            return;
        }
        
        if (codigo.length() != 6) {
             errorLabel.setText("O código deve ter 6 dígitos.");
             return;
        }

        // 2. Simulação
        if (codigo.equals(codigoCorreto)) {
            System.out.println("SIMULAÇÃO: Código correto.");
            
            // 3. Rota (Navegação)
            App.setRoot("AlterarSenha"); // Navega para a tela de alterar a senha
        } else {
            errorLabel.setText("Código inválido ou expirado. Tente novamente.");
        }
    }

    @FXML
    private void handleVoltar() throws IOException {
        // Volta para a tela de digitar o e-mail
        App.setRoot("EnviarEmailRecuperacao");
    }
}