package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField; // Use PasswordField para senhas

public class AlterarSenhaController {

    @FXML
    private PasswordField novaSenhaField; // Dê este fx:id ao campo "Nova Senha"

    @FXML
    private PasswordField confirmarSenhaField; // Dê este fx:id ao campo "Confirmar Senha"

    @FXML
    private Label errorLabel; // Dê este fx:id a um Label de erro

    @FXML
    private void handleRedefinirSenha() throws IOException {
        String novaSenha = novaSenhaField.getText();
        String confirmarSenha = confirmarSenhaField.getText();

        // 1. Validação
        if (novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            errorLabel.setText("Por favor, preencha ambos os campos.");
            return;
        }

        if (novaSenha.length() < 6) { // Exemplo de regra de negócio
            errorLabel.setText("A senha deve ter pelo menos 6 caracteres.");
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            errorLabel.setText("As senhas não conferem.");
            return;
        }

        // 2. Simulação (Aqui faríamos o UPDATE no banco)
        // Lembre-se, aqui teríamos que CRIPTOGRAFAR a senha antes de salvar!
        System.out.println("SIMULAÇÃO: Senha alterada com sucesso!");
        
        // 3. Rota (Navegação)
        // Senha alterada, manda de volta para o Login
        App.setRoot("Login"); 
    }
    
    @FXML
    private void handleVoltar() throws IOException {
        // Cancela a operação e volta para o Login
        App.setRoot("Login");
    }
}