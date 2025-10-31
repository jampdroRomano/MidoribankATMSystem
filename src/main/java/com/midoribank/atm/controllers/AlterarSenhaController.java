package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class AlterarSenhaController {

    @FXML
    private PasswordField novaSenhaField;

    @FXML
    private PasswordField confirmarSenhaField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleRedefinirSenha() throws IOException {
        String novaSenha = novaSenhaField.getText();
        String confirmarSenha = confirmarSenhaField.getText();

        if (novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            errorLabel.setText("Por favor, preencha ambos os campos.");
            return;
        }

        if (novaSenha.length() < 6) {
            errorLabel.setText("A senha deve ter pelo menos 6 caracteres.");
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            errorLabel.setText("As senhas não conferem.");
            return;
        }

        System.out.println("SIMULAÇÃO: Senha alterada com sucesso!");

        App.setRoot("Login");
    }

    @FXML
    private void handleVoltar() throws IOException {

        App.setRoot("Login");
    }
}
