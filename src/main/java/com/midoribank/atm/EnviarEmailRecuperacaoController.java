package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EnviarEmailRecuperacaoController {

    @FXML
    private TextField emailField; // Dê este fx:id ao seu campo de e-mail no FXML

    @FXML
    private Label errorLabel; // Dê este fx:id a um Label de erro no FXML

    @FXML
    private void handleEnviarCodigo() throws IOException {
        String email = emailField.getText();

        // 1. Validação
        if (email.isEmpty()) {
            errorLabel.setText("Por favor, informe seu e-mail.");
            return;
        }

        // Validação simples (só verifica se tem @ e .)
        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setText("Formato de e-mail inválido.");
            return;
        }

        // 2. Simulação (aqui nós conectaríamos com o banco de dados)
        // Por enquanto, vamos fingir que o e-mail "alvaro@midori.com" existe
        if (email.equals("alvaro@midori.com")) { 
            
            System.out.println("SIMULAÇÃO: E-mail encontrado. Enviando código...");
            
            // 3. Rota (Navegação)
            // Guarda o e-mail para usar na próxima tela (opcional, mas útil)
            // SessionManager.getInstance().setEmailRecuperacao(email); 
            
            App.setRoot("VerificarCodigoEmail"); // Navega para a tela de verificar o código
        } else {
            // Por segurança, NUNCA diga "E-mail não encontrado".
            // Apenas diga que o e-mail foi enviado.
            System.out.println("SIMULAÇÃO: Se o e-mail '" + email + "' existir, um código foi enviado.");
            // Mesmo se o e-mail não existir, mandamos para a próxima tela
            // para evitar que um invasor descubra quais e-mails estão cadastrados.
            App.setRoot("VerificarCodigoEmail");
        }
    }

    @FXML
    private void handleVoltar() throws IOException {
        // Supondo que você veio do Login
        App.setRoot("Login"); // Ou "opcoesLogin", de onde você veio
    }
}