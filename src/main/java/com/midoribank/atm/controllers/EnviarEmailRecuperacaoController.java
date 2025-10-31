package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EnviarEmailRecuperacaoController {

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleEnviarCodigo() throws IOException {
        String email = emailField.getText();

        if (email.isEmpty()) {
            errorLabel.setText("Por favor, informe seu e-mail.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setText("Formato de e-mail inválido.");
            return;
        }

        if (email.equals("alvaro@midori.com")) {

            System.out.println("SIMULAÇÃO: E-mail encontrado. Enviando código...");

            App.setRoot("VerificarCodigoEmail");
        } else {

            System.out.println("SIMULAÇÃO: Se o e-mail '" + email + "' existir, um código foi enviado.");

            App.setRoot("VerificarCodigoEmail");
        }
    }

    @FXML
    private void handleVoltar() throws IOException {

        App.setRoot("Login");
    }
}
